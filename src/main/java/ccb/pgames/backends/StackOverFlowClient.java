package ccb.pgames.backends;

import ccb.pgames.backends.StackOverFlow;
import ccb.pgames.model.FeaturedResponse;
import io.micronaut.http.client.annotation.Client;

@Client("https://api.stackexchange.com")
public interface StackOverFlowClient extends StackOverFlow {
    @Override
    FeaturedResponse latestQuestions(int page, int pagesize, String sort, String order, String site);
}
