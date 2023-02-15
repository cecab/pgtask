package ccb.pgames.backends;

import ccb.pgames.backends.models.Question;
import ccb.pgames.backends.models.User;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;

@Client(id = "stackexchange")
@Requires(missingProperty = "spec.name")
public interface StackOverFlow {
    @Get("/questions/featured")
    StackResponse<Question> latestQuestions(@QueryValue int page, @QueryValue int pagesize, @QueryValue String sort,
                                            @QueryValue("order") String order, @QueryValue("site") String site);

    @Get("/users/{userId}")
    StackResponse<User> userDetails(@PathVariable int userId, @QueryValue String order, @QueryValue String sort,
                                    @QueryValue String site);
}
