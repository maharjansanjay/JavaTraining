package controllers;

import models.Question;
import models.QuestionCategory;
import models.Result;
import models.User;
import services.LookUpItemService;
import services.QuestionService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "QuestionServlet")
public class QuestionServlet extends HttpServlet {
    private QuestionService questionService;
    private LookUpItemService _list;

    public QuestionServlet() {
        questionService = new QuestionService();
        _list = new LookUpItemService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            response.sendRedirect("/login");
            return;
        }

        String page = request.getParameter("page");

        switch (page) {
            case "AddQuestion":
            {
                String questionText = request.getParameter("questionText");
                String option1 = request.getParameter("option1");
                String option2 = request.getParameter("option2");
                String option3 = request.getParameter("option3");
                String option4 = request.getParameter("option4");
                String correctAns = request.getParameter("correctAns");
                String[] questionCategories = request.getParameterValues("questionCategories");

                List<QuestionCategory> questionCategoryList = new ArrayList();
                for (String questionCategory: questionCategories) {
                    questionCategoryList.add(new QuestionCategory(Integer.parseInt(questionCategory)));
                }

                questionService.addQuestion(new Question(0, questionText, option1, option2, option3, option4, Integer.parseInt(correctAns), questionCategoryList));

                response.sendRedirect("/question?page=QuestionList");
            }
            break;
            case "Quiz":
            {
                int quizId = (int) session.getAttribute("quizId");

                String qid = request.getParameter("qid");
                int questionId = Integer.parseInt(qid);
                String answer = request.getParameter("selection");

                questionService.saveAnswer(quizId, questionId, answer);

                Question question = questionService.getNextQuestion(questionId);

                if (question == null) {
                    response.sendRedirect("/question?page=Result");
                    return;
                }

                RequestDispatcher rd = request.getRequestDispatcher("Question/Quiz.jsp");
                request.setAttribute("question", question);
                rd.forward(request, response);
            }
            break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            response.sendRedirect("/login");
            return;
        }

        String page = request.getParameter("page");

        switch (page) {
            case "QuestionList":
            {
                List<Question> questions = questionService.getQuestions();
                RequestDispatcher rd = request.getRequestDispatcher("Question/QuestionList.jsp");
                request.setAttribute("questions", questions);
                rd.forward(request, response);
            }
            break;
            case "QuizList":
            {
                List<Result> results = questionService.getResults(loggedUser.getId());
                RequestDispatcher rd = request.getRequestDispatcher("Question/QuizList.jsp");
                request.setAttribute("quizList", results);
                rd.forward(request, response);
            }
            break;
            case "Quiz":
            {
                int quizId = questionService.startNewQuiz(loggedUser.getId());
                Question question = questionService.getNextQuestion(-1);

                RequestDispatcher rd = request.getRequestDispatcher("Question/Quiz.jsp");
                session.setAttribute("quizId", quizId);
                request.setAttribute("question", question);
                rd.forward(request, response);
            }
            break;
            case "AddQuestion":
            {
                List<QuestionCategory> questionCategories = _list.getQuestionCategories();
                RequestDispatcher rd = request.getRequestDispatcher("Question/AddQuestion.jsp");
                request.setAttribute("questionCategories", questionCategories);
                rd.forward(request, response);
            }
            break;
            case "DeleteQuestion":
            {
                String id = request.getParameter("id");

                questionService.deleteQuestion(Integer.parseInt(id));

                response.sendRedirect("/question?page=QuestionList");
            }
            break;
            case "Result":
            {
                RequestDispatcher rd = request.getRequestDispatcher("Question/Result.jsp");
                int quizId = (int) session.getAttribute("quizId");

                Result result = questionService.getResult(quizId);

                request.setAttribute("result", result);
                rd.forward(request, response);
            }
            break;
        }
    }
}
