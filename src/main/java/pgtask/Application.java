package pgtask;

import ccb.pgames.services.QuestionService;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Application {
    @Inject
    private QuestionService questionService;

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @EventListener
    public void onStart(StartupEvent event) {
        questionService.fetch();
    }
}