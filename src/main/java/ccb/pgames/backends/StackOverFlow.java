package ccb.pgames.backends;

import ccb.pgames.model.FeaturedResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

public interface StackOverFlow {
    //TODO: Move this value to main config for the App
    @Get("/2.3/questions/featured")
    FeaturedResponse latestQuestions(@QueryValue int page, @QueryValue int pagesize, @QueryValue String sort, @QueryValue("order") String order, @QueryValue("site") String site);
}
