package ccb.pgames;

import ccb.pgames.backends.StackOverFlowClient;
import ccb.pgames.model.Question;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.List;

@Named("fetcher")
@Singleton
public class QuestionLoader {
    List<Question> dbQuestions;

    public QuestionLoader(StackOverFlowClient fetcher) {
        dbQuestions = fetcher.latestQuestions(1, 20, "creation", "desc", "stackoverflow").getItems();
    }

    public int getSize() {
        return this.dbQuestions.size();
    }
}
