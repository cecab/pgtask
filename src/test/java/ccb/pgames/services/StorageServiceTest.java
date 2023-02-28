package ccb.pgames.services;

import ccb.pgames.backends.StackOverFlow;
import ccb.pgames.backends.StackResponse;
import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import ccb.pgames.controllers.models.QuestionAPI;
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

@Property(name = "spec.name", value = "StorageServiceTest")
@MicronautTest
class StorageServiceTest {

    @Inject
    EmbeddedServer embeddedServer;
    @Test
    void testGetByTagsForNoexistingTags() {
        Optional<List<QuestionAPI>> questionsFound = embeddedServer.getApplicationContext().findBean(StorageService.class)
                .map(storageService -> storageService.getByTags("tagNoValid"));

        Assertions.assertTrue(questionsFound.isPresent());
        Assertions.assertEquals(0,questionsFound.get().size());
    }

    @Test
    void testGetByTags() {
        var questionsTag1 = embeddedServer.getApplicationContext().findBean(StorageService.class)
                .map(storageService -> storageService.getByTags("tag1"));

        var questionsTag2 = embeddedServer.getApplicationContext().findBean(StorageService.class)
                .map(storageService -> storageService.getByTags("tag2"));

        Assertions.assertTrue(questionsTag1.isPresent());
        Assertions.assertTrue(questionsTag1.get().get(0).getId() > 0 );
        Assertions.assertEquals(2, questionsTag2.get().size());
    }


    @Requires(property = "spec.name", value = "StorageServiceTest")
    @Controller
    static class MockServer implements StackOverFlow {
        @Override
        public StackResponse<Question> latestQuestions(int page, int pagesize, String sort, String order, String site) {
            var resp = new StackResponse<Question>();
            var owner1 = new Question.Owner();
            owner1.setUser_id(8888);
            
            var q1 = new Question();
            q1.setView_count(10);
            q1.setTags(List.of("tag1","tag2"));
            q1.setTitle("Updating filter options from results (1M+ rows in db)");
            q1.setCreation_date(1677606000);
            q1.setOwner(owner1);
            q1.setIs_answered(false);
            q1.setAnswer_count(8);

            var q2 = new Question();
            q2.setView_count(10);
            q2.setTags(List.of("tag2","tag3"));
            q2.setTitle("Updating filter options from results (1M+ rows in db)");
            q2.setCreation_date(1677606000);
            q2.setOwner(owner1);
            q2.setIs_answered(false);
            q2.setAnswer_count(8);

            resp.setItems(List.of(q1,q2));
            resp.setHas_more(false);

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