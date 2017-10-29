package models;

public class QuestionCategory {
    private int id;
    private String category;

    public QuestionCategory(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public QuestionCategory(int id) {this.id = id;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
