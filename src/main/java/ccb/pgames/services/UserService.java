package ccb.pgames.services;

import ccb.pgames.backends.StackOverFlowClient;
import ccb.pgames.controllers.model.UserAPI;
import ccb.pgames.helpers.FormatHelper;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.Optional;

@Named("users")
@Singleton
public class UserService {
    private final StackOverFlowClient fetcher;

    public UserService(StackOverFlowClient fetcher) {
        this.fetcher = fetcher;
    }

    @Cacheable(cacheNames = "{userCache}")
    public Optional<UserAPI> getById(int userId) {
        return fetcher.userDetails(userId, "desc", "reputation", "stackoverflow").getItems().stream().findFirst().map(user -> {
            var apiUser = new UserAPI();
            apiUser.setUser_id(userId);
            apiUser.setCreation_date(FormatHelper.formatDate(user.getCreation_date()));
            apiUser.setDisplay_name(user.getDisplay_name());
            return apiUser;
        });
    }
}
