package pgtask;

import ccb.pgames.QuestionLoader;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;
import jakarta.inject.Named;

public class Application {


    @Inject
    @Named("fetcher")
    private QuestionLoader questionLoader;

    public static void main(String[] args) {
        var applicationContext = Micronaut.build(args)
                .eagerInitSingletons(true)
                .mainClass(Application.class)
                .args(args).start();
        //TODO: get() is tolerated here because we trust in the previous wiring of beans
        var fetcher = applicationContext.findBean(QuestionLoader.class).get();
        fetcher.fetch();
    }
}