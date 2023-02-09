package ccb.pgames.controllers;

import ccb.pgames.dao.QuestionDao;
import ccb.pgames.dao.model.QuestionDB;
import io.micronaut.http.annotation.*;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

@Controller("/questions")
public class QuestionController {
    private final Jdbi jdbi;

    public QuestionController(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    //TODO: tag is a list of possible tags, not just one.
    @Get
    public List<QuestionDB> getByTag(@QueryValue(value = "tags", defaultValue = "") String tag) {
        return jdbi.withExtension(QuestionDao.class, dao -> {
            if (tag.isBlank()) {
                return dao.findAll();
            }
            return dao.findByTag(tag);
        });
    }

    @Get("/{questionId}")
    public Optional<QuestionDB> getById(@PathVariable int questionId) {
        //TODO: Reformat creation_date
        return jdbi.withExtension(QuestionDao.class, dao -> dao.findById(questionId));
    }

    @Delete("/{questionId}")
    public Optional<Integer> deleteById(@PathVariable int questionId) {
        //TODO: Reformat creation_date
        int deleted = jdbi.withExtension(QuestionDao.class, dao -> dao.deleteById(questionId));
        if (deleted == 0) {
            return Optional.empty();
        }
        return Optional.of(deleted);
    }
}
