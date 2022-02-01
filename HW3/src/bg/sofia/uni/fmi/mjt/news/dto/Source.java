package bg.sofia.uni.fmi.mjt.news.dto;

import java.util.Objects;

public class Source {

    private String id;
    private String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return Objects.equals(id, source.getId()) && Objects.equals(name, source.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
