package ccb.pgames.dao.model;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDB {
    private int id;
    private String title;
    private List<String> tags;
    private Boolean isAnswered;
    private int viewCount;
    private int answerCount;
    private long creation_date;
    private int userId;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }
}
