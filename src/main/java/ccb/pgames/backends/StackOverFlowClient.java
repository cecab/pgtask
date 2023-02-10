package ccb.pgames.backends;

import ccb.pgames.model.Question;
import io.micronaut.http.client.annotation.Client;

@Client("https://api.stackexchange.com/2.3")
public interface StackOverFlowClient extends StackOverFlow {
    @Override
    StackResponse<Question> latestQuestions(int page, int pagesize, String sort, String order, String site);

    @Override
    StackResponse<User> userDetails(int userId, String order, String sort, String site);

}
