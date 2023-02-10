package pgtask;

import ccb.pgames.services.QuestionService;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;
import jakarta.inject.Named;

public class Application {


    @Inject
    @Named("fetcher")
    private QuestionService questionService;

    public static void main(String[] args) {
        var applicationContext = Micronaut.build(args)
                .eagerInitSingletons(true)
                .mainClass(Application.class)
                .args(args).start();
        //TODO: get() is tolerated here because we trust in the previous wiring of beans
        var fetcher = applicationContext.findBean(QuestionService.class).get();
        fetcher.fetch();
    }
}