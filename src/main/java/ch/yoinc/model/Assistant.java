package ch.yoinc.model;

public class Assistant {

    private String title;
    private String description;

    public Assistant(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
