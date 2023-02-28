package ccb.pgames.services;

import ccb.pgames.backends.StackOverFlow;
import ccb.pgames.backends.StackResponse;
import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import ccb.pgames.dao.QuestionDao;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "spec.name", value = "QuestionServiceTest")
@MicronautTest
class QuestionServiceTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    Jdbi jdbi;

    @Test
    void testsGetQuestionsByTags() {
        embeddedServer.getApplicationContext().findBean(QuestionService.class)
                .ifPresent(QuestionService::fetch);
        // Read from DB.
        var dbQuestions = jdbi.withExtension(QuestionDao.class, QuestionDao::findAll);

        Assertions.assertEquals(1, dbQuestions.size());
        Assertions.assertEquals(10, dbQuestions.get(0).getViewCount());
        Assertions.assertEquals(1677606000, dbQuestions.get(0).getCreation_date());
    }

    @Requires(property = "spec.name", value = "QuestionServiceTest")
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

            resp.setItems(List.of(q1));
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