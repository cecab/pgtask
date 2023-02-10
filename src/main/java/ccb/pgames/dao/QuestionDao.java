package ccb.pgames.dao;

import ccb.pgames.dao.models.QuestionDB;
import ccb.pgames.backends.models.Question;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface QuestionDao {
    @SqlQuery("SELECT * from question")
    List<QuestionDB> findAll();

    @SqlQuery("SELECT * from question where id=:id")
    Optional<QuestionDB> findById(@Bind("id") int id);

    @SqlQuery("SELECT * from question where :tag = ANY(tags)")
    List<QuestionDB> findByTag(@Bind("tag") String tag);

    default List<QuestionDB> findByTags(List<String> tags) {
        return tags.stream()
                .flatMap((String tag) -> findByTag(tag).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @SqlUpdate("INSERT INTO question(title,tags,is_answered,view_count,answer_count,creation_date,user_id) " +
            "values(:title,:tags, :is_answered, :view_count, :answer_count, :creation_date, :user_id)")
    int insert(@BindBean Question question);

    @SqlUpdate("DELETE FROM question")
    int clearAll();

    @SqlUpdate("DELETE from question where id=:id")
    int deleteById(@Bind("id") int id);
}
