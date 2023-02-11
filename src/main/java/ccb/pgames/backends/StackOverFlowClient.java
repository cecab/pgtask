package ccb.pgames.backends;

import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import io.micronaut.http.client.annotation.Client;

@Client(id="stackexchange")
public interface StackOverFlowClient extends StackOverFlow {
    @Override
    StackResponse<Question> latestQuestions(int page, int pagesize, String sort, String order, String site);

    @Override
    StackResponse<User> userDetails(int userId, String order, String sort, String site);

}
