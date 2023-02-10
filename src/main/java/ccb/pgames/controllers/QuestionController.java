package ccb.pgames.controllers;

import ccb.pgames.controllers.models.QuestionAPI;
import ccb.pgames.dao.QuestionDao;
import ccb.pgames.dao.models.QuestionDB;
import io.micronaut.http.annotation.*;
import org.jdbi.v3.core.Jdbi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/questions")
public class QuestionController {
    private final Jdbi jdbi;

    public QuestionController(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    private List<QuestionAPI> asQuestionAPI(List<QuestionDB> questions) {
        return questions.stream().map(QuestionAPI::new).collect(Collectors.toList());
    }

    @Get
    public List<QuestionAPI> getByTag(@QueryValue(value = "tags", defaultValue = "") String strTags) {
        return asQuestionAPI(jdbi.withExtension(QuestionDao.class, dao -> {
            var tags = Arrays.asList(strTags.split(","));
            if (tags.size() == 1 && tags.get(0).isEmpty()) {
                return dao.findAll();
            }
            return dao.findByTags(tags);
        }));
    }

    @Get("/{questionId}")
    public Optional<QuestionAPI> getById(@PathVariable int questionId) {
        return jdbi.withExtension(QuestionDao.class, dao -> dao.findById(questionId)).map(QuestionAPI::new);
    }

    @Delete("/{questionId}")
    public Optional<Integer> deleteById(@PathVariable int questionId) {
        int deleted = jdbi.withExtension(QuestionDao.class, dao -> dao.deleteById(questionId));
        if (deleted == 0) {
            return Optional.empty();
        }
        return Optional.of(deleted);
    }
}
