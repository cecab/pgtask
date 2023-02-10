package ccb.pgames.services;

import ccb.pgames.backends.StackOverFlowClient;
import ccb.pgames.dao.QuestionDao;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("questions")
@Singleton
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
    private final StackOverFlowClient fetcher;
    Jdbi jdbi;


    public QuestionService(StackOverFlowClient fetcher, Jdbi jdbi) {
        this.jdbi = jdbi;
        this.fetcher = fetcher;
    }

    public void fetch() {
        var dbQuestions = fetcher.latestQuestions(1, 20, "creation", "desc", "stackoverflow").getItems();
        logger.info("Cleared questions from DB: {}", jdbi.withExtension(QuestionDao.class, QuestionDao::clearAll));
        int inserted = dbQuestions.stream()
                .map(question -> jdbi.withExtension(QuestionDao.class, dao -> dao.insert(question))
                )
                .reduce(0,
                        Integer::sum);
        assert inserted == dbQuestions.size();
        logger.info("Fetched and persisted questions from DB: {}", inserted);
    }
}
