package ccb.pgames.backends.models;

import java.util.List;
import java.util.Objects;

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

    public Integer getUser_id() {
        return this.owner != null ? this.owner.getUser_id() : null;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return view_count == question.view_count && answer_count == question.answer_count && creation_date == question.creation_date && Objects.equals(title, question.title) && Objects.equals(tags, question.tags) && Objects.equals(is_answered, question.is_answered) && Objects.equals(owner, question.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, tags, is_answered, view_count, answer_count, creation_date, owner);
    }

    public static class Owner {
        int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Owner owner = (Owner) o;
            return user_id == owner.user_id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(user_id);
        }
    }
}


