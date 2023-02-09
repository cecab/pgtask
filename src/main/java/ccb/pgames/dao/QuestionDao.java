package ccb.pgames.dao;

import ccb.pgames.dao.model.QuestionDB;
import ccb.pgames.model.Question;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface QuestionDao {
    @SqlQuery("SELECT * from question")
    List<QuestionDB> findAll();

    @SqlUpdate("INSERT INTO question(title,tags,is_answered,view_count,answer_count,creation_date,user_id) " +
            "values(:title,:tags, :is_answered, :view_count, :answer_count, :creation_date, :user_id)")
    int insert(@BindBean Question question);

    @SqlUpdate("DELETE FROM question")
    int clearAll();
}
