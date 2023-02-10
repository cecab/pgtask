package ccb.pgames.controllers.models;

import ccb.pgames.dao.models.QuestionDB;
import ccb.pgames.helpers.FormatHelper;

import java.util.List;

public class QuestionAPI {
    public QuestionAPI(QuestionDB questionDB) {
        this.id = questionDB.getId();
        this.title = questionDB.getTitle();
        this.tags = questionDB.getTags();
        this.is_answered = questionDB.getAnswered();
        this.view_count = questionDB.getViewCount();
        this.answer_count = questionDB.getAnswerCount();
        this.setCreationDateFromEphoc(questionDB.getCreation_date());
        this.user_id = questionDB.getUserId();
    }

    private int id;
    private String title;
    private List<String> tags;
    private Boolean is_answered;
    private int view_count;
    private int answer_count;
    private String creation_date;
    private int user_id;

    public String getTitle() {
        return title;
    }


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getIs_answered() {
        return is_answered;
    }

    public void setIs_answered(Boolean is_answered) {
        this.is_answered = is_answered;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public void setCreationDateFromEphoc(long creation_date) {
        this.creation_date = FormatHelper.formatDate(creation_date);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
