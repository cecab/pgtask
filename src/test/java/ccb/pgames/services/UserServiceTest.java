package ccb.pgames.services;

import ccb.pgames.backends.StackOverFlow;
import ccb.pgames.backends.StackResponse;
import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import ccb.pgames.controllers.models.UserAPI;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "spec.name", value = "UserServiceTest")
@MicronautTest
class UserServiceTest {
    @Inject
    EmbeddedServer embeddedServer;

    @Test
    void testUserDetailsForNoExistingCases() {
        var userAPI = embeddedServer.getApplicationContext().findBean(UserService.class)
                .flatMap(userService -> userService.getById(100));

        Assertions.assertTrue(userAPI.isEmpty());
    }

    @Test
    void testUserDetailsForExistingCases() {
        var userAPI = embeddedServer.getApplicationContext().findBean(UserService.class)
                .flatMap(userService -> userService.getById(10));

        Assertions.assertEquals("John", userAPI.get().getDisplay_name());
    }

    @Requires(property = "spec.name", value = "UserServiceTest")
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
            var response = new StackResponse<User>();
            response.setItems(List.of());

            var user = new User();
            user.setDisplay_name("John");
            user.setCreation_date(1677606000);
            if ( userId == 10) {
                response.setItems(List.of(user));
            }

            return response;
        }
    }

}