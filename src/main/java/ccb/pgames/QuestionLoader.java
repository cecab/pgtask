package ccb.pgames;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class QuestionLoader {

    List<String> dbQuestions;

    public QuestionLoader() {
        dbQuestions = new ArrayList<>();
        dbQuestions.add("Q100");
        dbQuestions.add("Q200");
    }
}
