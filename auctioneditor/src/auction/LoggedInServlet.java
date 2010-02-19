package auction;

import java.io.IOException;
import java.util.Date;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoggedInServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(LoggedInServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String item = req.getParameter("item");
        Date date = new Date();
        String url = req.getParameter("url");
        String price = req.getParameter("price");
        Seller greeting = new Seller(user, item, date, url, price);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(greeting);
        } finally {
            pm.close();

         resp.sendRedirect("/enterItem.jsp");
        }
    }
}