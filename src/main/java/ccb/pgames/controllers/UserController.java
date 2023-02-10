package ccb.pgames.controllers;

import ccb.pgames.controllers.models.UserAPI;
import ccb.pgames.services.UserService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.Optional;

@Controller("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Get("/{userId}")
    public Optional<UserAPI> getUserById(@PathVariable int userId) {
        return userService.getById(userId);
    }
}
