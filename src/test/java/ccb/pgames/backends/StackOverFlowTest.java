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
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StackOverFlowTest {

    @Test
    void verifyQuestionsCanBeFetchedTest() {
        EmbeddedServer pgTask = ApplicationContext.run(EmbeddedServer.class);
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Map.of("micronaut.http.services" + ".stackexchange.url", "http://localhost:" + pgTask.getPort(),
                        "spec.name", "checkTest"
                )
        );
        var httpClient = embeddedServer.getApplicationContext().createBean(StackExchangeMockServer.class, embeddedServer.getURL());
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