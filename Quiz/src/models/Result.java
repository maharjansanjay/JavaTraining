package models;

public class Result {
    private int quizId;
    private int totalQuestionsAnswered;
    private int totalCorrectAnswers;

    public Result(int quizId, int totalQuestionsAnswered, int totalCorrectAnswers) {
        this.quizId = quizId;
        this.totalQuestionsAnswered = totalQuestionsAnswered;
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    public void setTotalQuestionsAnswered(int totalQuestionsAnswered) {
        this.totalQuestionsAnswered = totalQuestionsAnswered;
    }

    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(int totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }
}
