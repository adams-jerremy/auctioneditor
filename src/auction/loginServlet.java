package auction;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class loginServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");
        
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Hello, " + user.getNickname());
            ///resp.sendRedirect(userService.createLogoutURL(req.getRequestURI()));
            
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
        
        
    }
}