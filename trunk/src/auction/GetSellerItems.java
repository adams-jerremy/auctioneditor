package auction;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import auction.datastore.Email;
import auction.datastore.Seller;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GetSellerItems extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
        resp.setContentType("text/plain");
       // resp.getWriter().println("Hello, world");
        
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
        	PersistenceManager pmUser = auction.PMF.get().getPersistenceManager();
        	Query queryUser = pmUser.newQuery( Email.class);
        	queryUser.setFilter("email == uemail");
        	queryUser.declareParameters("String uemail");
        	  try
        	  {
        		List<Email> emails = (List<Email>) queryUser.execute(user.getEmail());
        		Email currentEmail = null;
        	
        		if( !emails.isEmpty() )
        		{
        			currentEmail = emails.get(0);	
        			//String query = "select from " + auction.Seller.class.getName()+ " where seller == " + user.getUserId();
        			PersistenceManager pm = auction.PMF.get().getPersistenceManager();
        			Query query = pm.newQuery( Seller.class);
//        			Query query = pm.newQuery("select id from" + auction.Seller.class);
        			query.setFilter("seller == ukey");
        			query.declareParameters("String ukey");
        		String qry = "select from " + Seller.class.getName() + " where seller == " + KeyFactory.keyToString(currentEmail.getKey());
        			//List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
        List<Seller> sellers = (List<Seller>)pm.newQuery(qry).execute();
        			//List<auction.Seller> sellers = (List<auction.Seller>) query.execute(KeyFactory.keyToString(currentEmail.getKey()));
        			if (!sellers.isEmpty()) 
        			{ 
        				resp.getWriter().println(" <table> " +
        						"<tr> " +
        						"<td><b>Item</b></td>" +
        						"<td><b>Price</b></td>" +
        						"<td><b>Link to Page</b></td>" +
        						"</tr> ");
        				for (Seller s : sellers) 
        				{
        					resp.getWriter().println(" <tr> " +
        							"<td> " + s.getItem() + "</td>" +
        							"<td> " + s.getPrice() + "</td>" +
        							"<td> <a href = " + s.getUrl() + ">" + s.getUrl() + "</a> </td>" +
        							"</tr> ");
        				}
        			resp.getWriter().println("</table>");
        			}		
        		}
        	  }
        	  finally
        	  {
        		  queryUser.closeAll();
        	  }            
        } else {
     
        }
        
        
    }
}