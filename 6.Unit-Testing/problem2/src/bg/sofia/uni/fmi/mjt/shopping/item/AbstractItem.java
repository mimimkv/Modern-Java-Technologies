package bg.sofia.uni.fmi.mjt.shopping.item;

import java.util.Objects;

public abstract class AbstractItem implements Item {

    private String id;

    public AbstractItem(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractItem that = (AbstractItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
