package ccb.pgames.dao;

import ccb.pgames.backends.models.Question;
import ccb.pgames.dao.models.QuestionDB;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class QuestionDaoTest {
    @Container
    public static GenericContainer POSTGRESQL_CONTAINER;

    static {
        POSTGRESQL_CONTAINER = new GenericContainer<>("postgres").withExposedPorts(5432).withEnv(Map.of(
                "POSTGRES_USER", "pgtask", "POSTGRES_PASSWORD", "pgtask-password"));
        POSTGRESQL_CONTAINER.start();
    }

    static EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class,
            Map.of("datasources.default" + ".url",
                    "jdbc:postgresql://" + POSTGRESQL_CONTAINER.getHost() + ":" + POSTGRESQL_CONTAINER.getFirstMappedPort() + "/postgres", "datasources.default.username", "pgtask", "datasources.default.password", "pgtask-password"));
    static Jdbi jdbi = embeddedServer.getApplicationContext().findBean(Jdbi.class).get();

    @BeforeEach
    void setUp() {
        jdbi.useHandle(handler -> handler.execute("DELETE FROM question"));
    }

    @Test
    void testDaoInsert() {
        // round-trip to DB
        var question = new Question();
        var owner = new Question.Owner();
        owner.setUser_id(1000);

        question.setTitle("Test question");
        question.setIs_answered(false);
        question.setCreation_date(1676374978);
        question.setOwner(owner);
        question.setView_count(200);
        Integer inserted = jdbi.withExtension(QuestionDao.class, dao -> dao.insert(question));

        assertEquals(1, inserted);
        List<QuestionDB> questionDBS = jdbi.withExtension(QuestionDao.class, QuestionDao::findAll);

        assertEquals(1, questionDBS.size());
        assertEquals(1, questionDBS.get(0).getId());
        assertEquals("Test question", questionDBS.get(0).getTitle());
        assertEquals(false, questionDBS.get(0).getAnswered());
        assertEquals(1676374978, questionDBS.get(0).getCreation_date());
        assertEquals(owner.getUser_id(), questionDBS.get(0).getUserId());
        assertEquals(0, questionDBS.get(0).getAnswerCount());
        assertEquals(200, questionDBS.get(0).getViewCount());

    }

    @Test
    void testDaoClearAll() {
        var question = new Question();
        var owner = new Question.Owner();
        owner.setUser_id(1000);

        question.setTitle("Test question");
        question.setIs_answered(false);
        question.setCreation_date(1676374978);
        question.setOwner(owner);
        jdbi.withExtension(QuestionDao.class, dao -> dao.insert(question));
        Integer cleared = jdbi.withExtension(QuestionDao.class, QuestionDao::clearAll);

        assertEquals(1, cleared);
        List<QuestionDB> questionDBS = jdbi.withExtension(QuestionDao.class, QuestionDao::findAll);
        assertEquals(0, questionDBS.size());
    }

    @Test
    void testDaoDeleteById() {
        var question = new Question();
        var owner = new Question.Owner();
        owner.setUser_id(1000);

        question.setTitle("Test question");
        question.setIs_answered(false);
        question.setCreation_date(1676374978);
        question.setOwner(owner);
        jdbi.withExtension(QuestionDao.class, dao -> dao.insert(question));
        List<QuestionDB> questionDBS = jdbi.withExtension(QuestionDao.class, QuestionDao::findAll);
        Integer removed = jdbi.withExtension(QuestionDao.class, dao -> dao.deleteById(questionDBS.get(0).getId()));
        Integer failedRemoved = jdbi.withExtension(QuestionDao.class,
                dao -> dao.deleteById(questionDBS.get(0).getId() + 10));

        assertEquals(1, removed);
        assertEquals(0, failedRemoved);
    }

    @Test
    void testDaoFindByTags() {
        var owner = new Question.Owner();
        owner.setUser_id(1000);
        // Q1
        var question1 = new Question();
        question1.setTitle("Test question");
        question1.setIs_answered(false);
        question1.setCreation_date(1676374978);
        question1.setOwner(owner);
        question1.setTags(List.of("tag1", "tag2"));
        jdbi.withExtension(QuestionDao.class, dao -> dao.insert(question1));
        // Q2 
        var question2 = new Question();
        question2.setTitle("Test question");
        question2.setIs_answered(false);
        question2.setCreation_date(1676374978);
        question2.setOwner(owner);
        question2.setTags(List.of("tag1", "tag3"));
        jdbi.withExtension(QuestionDao.class, dao -> dao.insert(question2));

        List<QuestionDB> questionsTag1 = jdbi.withExtension(QuestionDao.class, dao -> dao.findByTags(List.of("tag1")));
        List<QuestionDB> questionsTag3 = jdbi.withExtension(QuestionDao.class, dao -> dao.findByTags(List.of("tag3")));
        List<QuestionDB> questionsTag4 = jdbi.withExtension(QuestionDao.class, dao -> dao.findByTags(List.of("tag4")));

        assertEquals(2, questionsTag1.size());
        assertEquals(1, questionsTag3.size());
        assertEquals(0, questionsTag4.size());
    }
}