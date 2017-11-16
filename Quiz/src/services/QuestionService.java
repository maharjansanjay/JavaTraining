package services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dataAccess.DatabaseConnection;
import models.QuestionCategory;
import models.Question;
import models.Result;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class QuestionService {
    private DatabaseConnection db;

    public QuestionService()
    {
        db = new DatabaseConnection();
    }

    public List<Question> getQuestions() {
        List<domain.Question> rawQuestions = new ArrayList<>();
        PreparedStatement ps = db.getPreparedStatement("select q.id as questionId, q.questionText, q.option1, q.option2, q.option3, q.option4, q.correctAns," +
                "qc.id as categoryId, qc.Description as category from quiz.questions q join questionCategoriesMap qcm on qcm.questionId" +
                "= q.id join questionCategories qc on qc.id = qcm.categoryId ");

        try {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                rawQuestions.add(new domain.Question(rs.getInt("questionId"),rs.getString("questionText"),rs.getString("option1"),
                        rs.getString("option2"), rs.getString("option3"), rs.getString("option4"),rs.getInt("correctAns"),
                        rs.getInt("categoryId"), rs.getString("category")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Map<Object,List<QuestionCategory>> groupedQuestion= rawQuestions.stream().collect(
                groupingBy(x-> Arrays.<Object>asList(x.getId(), x.getQuestionText(), x.getOption1(), x.getOption2(), x.getOption3(), x.getOption4(), x.getCorrectAns()),
                        Collectors.mapping((domain.Question x) -> new QuestionCategory(x.getCategoryId(),x.getCategory()),toList()))
        );

        List<Question> questions = new ArrayList<Question>();

        for (Map.Entry<Object,List<QuestionCategory>> item: groupedQuestion.entrySet()) {
            List<Object> key = (List<Object>)item.getKey();
            questions.add(new Question((int)key.get(0),(String) key.get(1),(String) key.get(2),(String)key.get(3),(String) key.get(4),(String) key.get(5), (int) key.get(6),item.getValue()));
        }

        questions.sort((o1, o2)->o1.getId()-o2.getId());
        return questions;
    }

    public Question getQuestion(int id) {
        List<domain.Question> rawQuestions = new ArrayList<>();
        PreparedStatement ps = db.getPreparedStatement("select q.id as questionId, q.questionText, q.option1, q.option2, q.option3, q.option4, q.correctAns," +
                "qc.id as categoryId, qc.Description as category from quiz.questions q join questionCategoriesMap qcm on qcm.questionId" +
                "= q.id join questionCategories qc on qc.id = qcm.categoryId where q.id = ?");

        try {
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                rawQuestions.add(new domain.Question(rs.getInt("questionId"),rs.getString("questionText"),rs.getString("option1"),
                        rs.getString("option2"), rs.getString("option3"), rs.getString("option4"),rs.getInt("correctAns"),
                        rs.getInt("categoryId"), rs.getString("category")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Map<Object,List<QuestionCategory>> groupedQuestion= rawQuestions.stream().collect(
                groupingBy(x-> Arrays.<Object>asList(x.getId(), x.getQuestionText(), x.getOption1(), x.getOption2(), x.getOption3(), x.getOption4(), x.getCorrectAns()),
                        Collectors.mapping((domain.Question x) -> new QuestionCategory(x.getCategoryId(),x.getCategory()),toList()))
        );

        Question question = null;

        for (Map.Entry<Object,List<QuestionCategory>> item: groupedQuestion.entrySet()) {
            List<Object> key = (List<Object>)item.getKey();
            question = new Question((int)key.get(0),(String) key.get(1),(String) key.get(2),(String)key.get(3),(String) key.get(4),(String) key.get(5), (int) key.get(6),item.getValue());
        }

        return question;
    }

    public Question getNextQuestion(int prevId) {

        if (prevId <= 0) {
            return getQuestion(1);
        }

        List<Question> questions = getQuestions();
        Boolean currentLocated = false;
        Question nextQuestion = null;

        for (Question question: questions) {
            if (currentLocated) {
                nextQuestion = question;
                break;
            }
            if (question.getId() == prevId) {
                currentLocated = true;
            }
        }

        return nextQuestion;
    }

    public void addQuestion(Question question) {
        String query = question.getId() > 0 ? "update quiz.questions q set q.questionText = ?, q.option1 = ?, q.option2 = ?, q.option3 = ?, q.option4 = ?, q.correctAns = ? where q.id = ?":
                "insert into quiz.questions (questionText, option1, option2, option3 , option4, correctAns) values (?,?,?,?,?,?)";
        PreparedStatement ps = db.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getOption1());
            ps.setString(3, question.getOption2());
            ps.setString(4, question.getOption3());
            ps.setString(5, question.getOption4());
            ps.setInt(6, question.getCorrectAns());

            if(question.getId() > 0)
            {
                ps.setInt(7, question.getId());
            }
            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0)
            {
                throw new SQLException("Creating Question failed");
            }

            int newQuestionId;
            if(question.getId() > 0)
            {
                newQuestionId = question.getId();
            }
            else {
                ResultSet generatedkeys = ps.getGeneratedKeys();

                if (generatedkeys.next()) {
                    newQuestionId = generatedkeys.getInt(1);
                } else {
                    throw new SQLException("Creating Question failed, no id obtained");
                }
            }

            addQuestionCategoriesMap(question.getCategories(),newQuestionId);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addQuestionCategoriesMap(List<QuestionCategory> categories,int questionId)
    {
        deleteQuestionCategoriesMap(questionId);
        PreparedStatement ps = db.getPreparedStatement("insert into questionCategoriesMap values(?,?)");
        try {
            for (QuestionCategory category : categories) {
                ps.setInt(1, questionId);
                ps.setInt(2,category.getId());
                ps.addBatch();
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int startNewQuiz(int id) {
        int quizId = -1;
        String query = "INSERT INTO `quiz`.`quizuser` (`userId`) VALUES (?);";

        PreparedStatement ps = db.getPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);

        try {
            ps.setInt(1, id);

            int updatedRows = ps.executeUpdate();

            if (updatedRows == 0 ) {
                throw new SQLException("Couldn't start new quiz");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();

            if (generatedKeys.next()) {
                quizId = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Couldn't fetch generated quiz id.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return quizId;
    }

    public void saveAnswer(int quizId, int questionId, String answer) {
        PreparedStatement ps = db.getPreparedStatement("INSERT INTO `quiz`.`answers` (`questionId`, `quizId`, `answer`) VALUES (?, ?, ?)");

        try {
            ps.setInt(1, questionId);
            ps.setInt(2, quizId);
            ps.setInt(3, Integer.parseInt(answer));
            ps.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public Result getResult(int quizId) {
        PreparedStatement ps = db.getPreparedStatement("select count(answers.questionId) total, " +
                "sum(case when answers.answer = questions.correctAns then 1 else 0 end) correct " +
                "from answers " +
                "join questions on questions.id = answers.questionId " +
                "where answers.quizid = ?");
        Result result = null;

        try {
            ps.setInt(1, quizId);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            int total = 0, correct = 0;

            if (rs.next()) {
                total = rs.getInt("total");
                correct = rs.getInt("correct");

                result = new Result(quizId, total, correct);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Result> getResults(int userId) {
        PreparedStatement ps = db.getPreparedStatement("select answers.quizId qid, count(answers.questionId) total, " +
                "sum(case when answers.answer = questions.correctAns then 1 else 0 end) correct " +
                "from answers " +
                "join questions on questions.id = answers.questionId " +
                "join quizuser on quizuser.quizId = answers.quizId " +
                "where quizuser.userId = ? " +
                "group by answers.quizid");
        List<Result> result = new ArrayList<Result>();

        try {
            ps.setInt(1, userId);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            int quizId = 0, total = 0, correct = 0;

            while (rs.next()) {
                quizId = rs.getInt("qid");
                total = rs.getInt("total");
                correct = rs.getInt("correct");

                result.add(new Result(quizId, total, correct));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void deleteQuestionCategoriesMap(int questionId)
    {
        PreparedStatement ps = db.getPreparedStatement("delete from questionCategoriesMap where questionId = ?");
        try {
            ps.setInt(1, questionId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(int id) {
        deleteQuestionCategoriesMap(id);
        PreparedStatement ps = db.getPreparedStatement("delete from quiz.questions where id = ?");
        try {
            ps.setInt(1, id);
            ps.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
