package ccb.pgames.model;

import java.util.List;

public class FeaturedResponse {
    private List<Question> items;
    private boolean has_more;

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }
}
