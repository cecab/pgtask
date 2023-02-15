package ccb.pgames.controllers;

import ccb.pgames.backends.StackOverFlow;
import ccb.pgames.backends.StackResponse;
import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;


@MicronautTest
@Property(name = "spec.name", value = "QuestionControllerTest")
class QuestionControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    Jdbi jdbi;

    @BeforeEach
    void setUp() {
        jdbi.useHandle(handler -> handler.execute("DELETE FROM question"));
    }

    @Test
    void testsQuestionsAreEmpty() throws JsonProcessingException {
        String questions = client.toBlocking().retrieve("/questions");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(questions);

        Assertions.assertTrue(jsonNode.isArray());
        Assertions.assertEquals(0, jsonNode.size());
    }

    @Test
    void checkUserDetailsForNotFoundUsers() {
        var userResponse = Assertions.assertThrowsExactly(HttpClientResponseException.class,
                () -> client.toBlocking().exchange("/user/10"));

        Assertions.assertEquals(404, userResponse.getStatus().getCode());
    }


    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Controller
    static class MockServer implements StackOverFlow {
        @Override
        public StackResponse<Question> latestQuestions(int page, int pagesize, String sort, String order, String site) {
            var resp = new StackResponse<Question>();
            resp.setItems(List.of());
            return resp;
        }

        @Override
        public StackResponse<User> userDetails(int userId, String order, String sort, String site) {
            return null;
        }
    }

}