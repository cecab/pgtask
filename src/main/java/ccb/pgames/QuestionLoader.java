package ccb.pgames;

import ccb.pgames.backends.StackOverFlowClient;
import ccb.pgames.dao.QuestionDao;
import ccb.pgames.dao.model.QuestionDB;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Named("questions")
@Singleton
public class QuestionLoader {
    private static final Logger logger = LoggerFactory.getLogger(QuestionLoader.class);
    private final StackOverFlowClient fetcher;
    Jdbi jdbi;


    public QuestionLoader(StackOverFlowClient fetcher, Jdbi jdbi) {
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
