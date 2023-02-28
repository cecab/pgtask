package ccb.pgames.controllers;

import ccb.pgames.backends.StackOverFlow;
import ccb.pgames.backends.StackResponse;
import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import ccb.pgames.controllers.models.UserAPI;
import ccb.pgames.dao.QuestionDao;
import ccb.pgames.helpers.TestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@MicronautTest(transactional = false)
@Property(name = "spec.name", value = "QuestionControllerOperationsTest")
class QuestionControllerOperationsTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    Jdbi jdbi;

    @Test
    void testsGetAllQuestions() throws JsonProcessingException {
        String questions = client.toBlocking().retrieve("/questions");
        ObjectMapper objectMapper = TestHelper.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(questions);

        Assertions.assertTrue(jsonNode.isArray());
        Assertions.assertTrue(jsonNode.size() > 0);
    }

    @Test
    void testsGetQuestionsByTags() throws JsonProcessingException {
        var request = HttpRequest.GET("/questions?tags=xyz,javascript");
        String questions = client.toBlocking().retrieve(request);
        ObjectMapper objectMapper = TestHelper.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(questions);

        Assertions.assertTrue(jsonNode.isArray());
        Assertions.assertTrue(jsonNode.size() > 0);
    }

    @Test
    void testsDeleteNonExistingQuestionById() {
        var removed = client.toBlocking().exchange(HttpRequest.DELETE("/questions/2023"));
        Assertions.assertEquals(202, removed.status().getCode());
    }

    @Test
    void checkUserDetails() throws JsonProcessingException {
        var response = client.toBlocking().retrieve("/userDetails/10");
        ObjectMapper objectMapper = TestHelper.getObjectMapper();
        var user = objectMapper.readValue(response, UserAPI.class);

        Assertions.assertEquals("John Smith", user.getDisplay_name());
        Assertions.assertEquals(10, user.getUser_id());
    }

    @Test
    void testsDeleteQuestionById() {
        var questions = jdbi.withExtension(QuestionDao.class, QuestionDao::findAll);
        var removed = client.toBlocking().exchange(HttpRequest.DELETE("/questions/" + questions.get(0).getId()));

        Assertions.assertEquals(204, removed.status().getCode());
    }

    @Requires(property = "spec.name", value = "QuestionControllerOperationsTest")
    @Controller
    static class MockServer implements StackOverFlow {

        private final ResourceLoader resourceLoader;

        MockServer(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public StackResponse<Question> latestQuestions(int page, int pagesize, String sort, String order, String site) {
            Optional<InputStream> resourceAsStream = resourceLoader.getResourceAsStream("questions-operations.json");
            ObjectMapper objectMapper = TestHelper.getObjectMapper();
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
            var user = new User();
            user.setCreation_date(1676455101);
            user.setDisplay_name("John Smith");

            var response = new StackResponse<User>();
            response.setItems(List.of(user));
            return response;
        }
    }

}