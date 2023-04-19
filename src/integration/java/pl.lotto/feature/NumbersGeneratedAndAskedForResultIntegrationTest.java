package pl.lotto.feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import config.IntegrationTestConfiguration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.AdjustableClock;
import pl.lotto.LotterryNumbergeneratorApplication;
import pl.lotto.numbergenerator.DrawingResultDto;
import pl.lotto.numbergenerator.NumberGeneratorFacade;
import pl.lotto.numbergenerator.WinningNumbersNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {LotterryNumbergeneratorApplication.class, IntegrationTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
public class NumbersGeneratedAndAskedForResultIntegrationTest {

    @Autowired
    AdjustableClock clock;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    NumberGeneratorFacade numberGeneratorFacade;
    @Autowired
    public ObjectMapper objectMapper;

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    public static final String WIRE_MOCK_HOST = "http://localhost";

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("lotto.lotto-client.baseURL", () -> WIRE_MOCK_HOST + ":" + wireMockServer.getPort());
    }

    @Test
    public void should_generate_and_answer_to_client() throws Exception {

        // step 0: generating core app service
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/lottery/nextDraw"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"drawDate":"2023-04-22T12:00:00"}
                                """
                                .trim())));

        // step 1: generating winning numbers
        // given
        // when
        LocalDateTime drawDate = LocalDateTime.of(2023, 4, 22, 12, 0);
        Awaitility.await()
                .pollInterval(Duration.ofSeconds(1))
                .atMost(Duration.ofSeconds(10))
                .until(() -> {
                    try {
                        return !numberGeneratorFacade.retrieveNumbersByDate(drawDate).numbers().isEmpty();
                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });
        // then

        // step 2: core app made GET /winnum/{date} system returned 200 OK
        //given
        //when
        ResultActions response = mockMvc.perform(get("/winnum/" + drawDate));
        //then

        String jsoResponse = response
                .andReturn()
                .getResponse()
                .getContentAsString();
        DrawingResultDto drawingResultDto = objectMapper
                .readValue(jsoResponse, DrawingResultDto.class);

        assertThat(drawingResultDto.date()).isEqualTo(drawDate);
        assertThat(drawingResultDto.numbers()).isNotEmpty();
    }
}
