package util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponserHelper {
    public static void ResponseJson(HttpServletResponse response, Object o) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(o));
        response.getWriter().flush();
    }
}
