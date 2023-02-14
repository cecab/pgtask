package ccb.pgames.services;

import ccb.pgames.controllers.models.QuestionAPI;
import ccb.pgames.dao.QuestionDao;
import ccb.pgames.dao.models.QuestionDB;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Named("storage")
@Singleton
public class StorageService {
    private final Jdbi jdbi;

    public StorageService(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    private List<QuestionAPI> asQuestionAPI(List<QuestionDB> questions) {
        return questions.stream().map(QuestionAPI::new).collect(Collectors.toList());
    }

    public List<QuestionAPI> getByTags(String strTags) {
        return asQuestionAPI(jdbi.withExtension(QuestionDao.class, dao -> {
            var tags = Arrays.asList(strTags.split(","));
            if (tags.size() == 1 && tags.get(0).isEmpty()) {
                return dao.findAll();
            }
            return dao.findByTags(tags);
        }));
    }

    public Optional<QuestionAPI> getById(int questionId) {
        return jdbi.withExtension(QuestionDao.class, dao -> dao.findById(questionId)).map(QuestionAPI::new);
    }

    public int deleteById(int questionId) {
        return jdbi.withExtension(QuestionDao.class, dao -> dao.deleteById(questionId));
    }
}
