package ch.yoinc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Assistant {
    private String id;
    private String title;
    private String description;

    public Assistant() {}

    public Assistant(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
