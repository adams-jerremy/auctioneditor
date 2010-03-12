package auction;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auction.datastore.Bid;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class BidServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BidServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
	        String query = "select key from "+auction.datastore.Email.class.getName()+" where email == \""+user.getEmail()+"\"";
	        Key userKey = ((List<Key>)pm.newQuery(query).execute()).get(0);
	        String item = req.getParameter("item");
	        int price = Integer.parseInt(req.getParameter("bid"));
	        Bid bid = new Bid(userKey, KeyFactory.stringToKey(item), price, new Date());        
	        pm.makePersistent(bid);
	        StringBuilder sb = new StringBuilder("http://porco-rosso.cs.utexas.edu:8083/submitBid?itemId=").append(item).append("&price=").append(price).append("&userId=").append(KeyFactory.keyToString(userKey));
	        URL url = new URL(sb.toString());
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.connect();
	        conn.getInputStream();
	        System.err.println(conn.getURL());
        } finally { pm.close();resp.sendRedirect("/editor");}
    }
}