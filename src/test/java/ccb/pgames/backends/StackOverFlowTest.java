package ccb.pgames.backends;

import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.annotation.Controller;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO: We do not need Jdbi for this test. The dependency on testContainer must be removed.
@Testcontainers
class StackOverFlowTest {
    @Container
    public static GenericContainer POSTGRESQL_CONTAINER;
    static final EmbeddedServer embeddedServer;

    private static Map<String, Object> buildDataSourceSettings(GenericContainer POSTGRESQL_CONTAINER) {
        String host = POSTGRESQL_CONTAINER.getHost();
        Integer mappedPort = POSTGRESQL_CONTAINER.getFirstMappedPort();
        return Map.of("datasources.default.url", "jdbc:postgresql://" + host + ":" + mappedPort + "/postgres",
                "datasources.default.username", "pgtask", "datasources.default.password", "pgtask-password");
    }

    static {
        POSTGRESQL_CONTAINER = new GenericContainer<>("postgres").withExposedPorts(5432).withEnv(Map.of(
                "POSTGRES_USER", "pgtask", "POSTGRES_PASSWORD", "pgtask-password"));
        POSTGRESQL_CONTAINER.start();

        embeddedServer = ApplicationContext.run(EmbeddedServer.class, buildDataSourceSettings(POSTGRESQL_CONTAINER));
    }

    @Test
    void verifyQuestionsCanBeFetchedTest() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("micronaut.http.services" + ".stackexchange.url", "http://localhost:" + 18700); //TODO: make
        // 18700 a dynamic free port available discovered
        settings.put("spec.name", "checkTest");
        settings.putAll(buildDataSourceSettings(POSTGRESQL_CONTAINER));
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class, settings);
        var httpClient = embeddedServer.getApplicationContext().createBean(StackExchangeMockServer.class,
                embeddedServer.getURL());
        var rsp = httpClient.latestQuestions(1, 20, "desc", "creation", "stackoverflow");
        assertEquals(5, rsp.getItems().size());
    }

    @Requires(property = "spec.name", value = "checkTest")
    @Controller
    static class StackExchangeMockServer implements StackOverFlow {
        private final ResourceLoader resourceLoader;

        StackExchangeMockServer(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public StackResponse<Question> latestQuestions(int page, int pagesize, String sort, String order, String site) {
            Optional<InputStream> resourceAsStream = resourceLoader.getResourceAsStream("questions.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return objectMapper.readValue(resourceAsStream.get(), new TypeReference<>() {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public StackResponse<User> userDetails(int userId, String order, String sort, String site) {
            return null;
        }
    }
}