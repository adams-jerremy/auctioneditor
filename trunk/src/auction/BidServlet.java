package auction;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import datastore.Bid;
import datastore.Email;

public class BidServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BidServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
	        String query = "select key from "+Email.class.getName()+" where email == \""+user.getEmail()+"\"";
	        Key userKey = ((List<Key>)pm.newQuery(query).execute()).get(0);
	        String item = req.getParameter("item");
	        Date date = new Date();
	        int price = Integer.parseInt(req.getParameter("bid"));
	        Bid bid = new Bid(userKey, KeyFactory.stringToKey(item), price, date);        
	        pm.makePersistent(bid);
        } finally { pm.close();resp.sendRedirect("/guestbook");}
    }
}