package controller;

import com.google.gson.Gson;
import model.User;
import util.BCrypt;
import util.GlobalConfigHelper;
import util.ResponserHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class AuthController extends javax.servlet.http.HttpServlet {
    private Properties properties = null;

    @Override
    public void init() {
        properties = GlobalConfigHelper.getConfigFromContext(this.getServletContext());
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request,
                          javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        switch (request.getRequestURI()) {
            case "/auth/register/check-username":
                checkUsername(request, response);
                break;
            case "/auth/register":
                register(request, response);
                break;
            case "/auth/login":
                login(request, response);
                break;
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request,
                         javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(
                properties.getProperty("TemplatePathRoot") + "login.jsp");
        dispatcher.forward(request, response);
    }

    public class BasicResponse {
        public int code;
        public boolean result;
        public String msg;

        public BasicResponse() {
            this.code = 0;
            this.result = false;
            this.msg = "";
        }

        public BasicResponse(int code, boolean result, String msg) {
            this.code = code;
            this.result = result;
            this.msg = msg;
        }
    }

    private void checkUsername(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        User u = new User(properties);
        if (request.getParameter("username").isEmpty() ||
                u.getUserByUsername(request.getParameter("username")) != null) {
            ResponserHelper.ResponseJson(response,
                    new BasicResponse(HttpServletResponse.SC_OK, false, "用户名已存在"));
        } else {
            ResponserHelper.ResponseJson(response,
                    new BasicResponse(HttpServletResponse.SC_OK, true, "用户名可用"));
        }
    }

    private void register(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        String repasswd = request.getParameter("repassword");
        if (username.isEmpty() || passwd.isEmpty() ||
                repasswd.isEmpty() || !passwd.equals(repasswd)) {
            request.getSession().setAttribute("error", "注册信息有误，请检查！");
            ResponserHelper.ResponseJson(response,
                    new BasicResponse(HttpServletResponse.SC_OK, false, "注册信息有误，请检查！"));
            return;
        }
        User u = new User(properties, -1, username, BCrypt.hashpw(passwd, BCrypt.gensalt()));
        if (u.insert() < 0) {
            ResponserHelper.ResponseJson(response,
                    new BasicResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            false, "注册失败，未知错误！"));
        } else {
            ResponserHelper.ResponseJson(response,
                    new BasicResponse(HttpServletResponse.SC_OK, true, "注册成功，请登录！"));
        }
    }

    private void login(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        User u = new User(properties);
        User user = u.getUserByUsername(username);
        // Get latest articles
        if (user == null) {
            ResponserHelper.ResponseJson(response,
                    new BasicResponse(HttpServletResponse.SC_OK, false, "用户名不存在！"));
        } else {
            if (BCrypt.checkpw(passwd, user.getPassword())) {
                // Login ok
                request.getSession(true).setAttribute("uid", user.getId());
                ResponserHelper.ResponseJson(response,
                        new BasicResponse(HttpServletResponse.SC_OK, true, "登录成功！"));
            } else {
                ResponserHelper.ResponseJson(response,
                        new BasicResponse(HttpServletResponse.SC_OK, false, "密码错误！"));
            }
        }
    }
}
