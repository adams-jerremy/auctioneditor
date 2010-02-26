package auction;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import com.google.appengine.api.datastore.Key;
import javax.jdo.Query;

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
        
        /*
        PersistenceManager pmUser = auction.PMF.get().getPersistenceManager();
    	Query query = pmUser.newQuery("select id from" + auction.UserInfo.class);
    	query.setFilter("userid == uid");
    	query.declareParameters("String uid");
    	
    	//List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
    	List userKeys = (List) query.execute(user.getUserId());
        
    	if(userKeys.isEmpty())
    	{
    		UserInfo newUser = new UserInfo(user);
    		PersistenceManager pmU = PMF.get().getPersistenceManager();
    		try
    		{
    			pmU.makePersistent(newUser);	
    		}
    		finally 
    		{
    			pmU.close();
    		}
    		userKeys = (List) query.execute(user.getUserId());
    	}
    	Key userKey = (Key)userKeys.get(0);
    	pmUser.close();
    	System.out.print(userKey);
    	
//        Key seller;
    	*/
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