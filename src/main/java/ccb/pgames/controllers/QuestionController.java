package ccb.pgames.controllers;

import ccb.pgames.controllers.models.QuestionAPI;
import ccb.pgames.services.StorageService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/questions")
public class QuestionController {
    private final StorageService storageService;

    public QuestionController(StorageService storageService) {
        this.storageService = storageService;
    }


    @Get
    public List<QuestionAPI> getByTag(@QueryValue(value = "tags", defaultValue = "") String strTags) {
        return storageService.getByTags(strTags);
    }

    @Get("/{questionId}")
    public Optional<QuestionAPI> getById(@PathVariable int questionId) {
        return storageService.getById(questionId);
    }

    @Delete("/{questionId}")
    public HttpResponse<Map<String,String>> deleteById(@PathVariable int questionId) {
        int deleted = storageService.deleteById(questionId);
        if (deleted == 0) {
            return HttpResponse.status(HttpStatus.valueOf(202))
                    .body(Map.of("error", "Question ID " + questionId + " was not removed"));
        }
        return HttpResponse.status(HttpStatus.valueOf(204));
    }
}
