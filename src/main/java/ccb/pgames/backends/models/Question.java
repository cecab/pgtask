package ccb.pgames.backends.models;

import java.util.List;

public class Question {
    private String title;
    private List<String> tags;
    private Boolean is_answered;
    private int view_count;
    private int answer_count;
    private long creation_date;
    private Owner owner;

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


    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public int getUser_id() {
        return this.owner.getUser_id();
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    static class Owner {
        int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}


