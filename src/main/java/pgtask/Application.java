package pgtask;

import ccb.pgames.QuestionLoader;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

public class Application {


    @Inject
    @Named("fetcher")
    private QuestionLoader questionLoader;

    public static void main(String[] args) {
        ApplicationContext applicationContext = Micronaut.build(args).eagerInitSingletons(true).mainClass(Application.class).args(args).start();
        Optional<QuestionLoader> fetcher = applicationContext.findBean(QuestionLoader.class);
        System.out.println("Fetched questions: " + fetcher.get().getSize());

    }
}