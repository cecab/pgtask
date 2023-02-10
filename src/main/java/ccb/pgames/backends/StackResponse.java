package ccb.pgames.backends;

import java.util.List;

public class StackResponse<T> {
    private List<T> items;
    private boolean has_more;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }
}
