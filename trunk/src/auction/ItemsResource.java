package auction;

import java.util.List;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import auction.datastore.Email;
import auction.datastore.Seller;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.dev.jjs.ast.js.JsonObject;




/**
 * Resource which has only one representation.
 * 
 */
public class ItemsResource extends ServerResource {

	@Get
	public String getItemList() {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
//        String str = request.getEntityAsText();
        String userName;// = str;
        userName = this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("userName").toString();
        if (user != null) {
     	   if( user.getEmail().split("@")[0].compareTo(userName) == 0 )
            {
     		  String currentURI = this.getReference().toUri().toString();
     		  System.out.println(currentURI);
     		  
     // ToDo: might be a better way of checking here....
     		  if( currentURI.contains("/items/create?") )
     		  {
     			  return this.createItem();
     		  }
     		  
     		   String resp = new String();
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
//            			Query query = pm.newQuery("select id from" + auction.Seller.class);
            			query.setFilter("seller == ukey");
            			query.declareParameters("String ukey");
            		String qry = "select from " + Seller.class.getName() + " where seller == " + KeyFactory.keyToString(currentEmail.getKey());
            			//List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
            List<Seller> sellers = (List<Seller>)pm.newQuery(qry).execute();
            			//List<auction.Seller> sellers = (List<auction.Seller>) query.execute(KeyFactory.keyToString(currentEmail.getKey()));
            
            		JSONObject itemsJson = new JSONObject();
            		JSONArray itemsArray = new JSONArray();
            			if (!sellers.isEmpty()) 
            			{ 
            				resp = " <table cellpadding=\"5\"> " +
            						"<tr> " +
            						"<td><b>Item# </b></td>" +
            						"<td><b>Item Name </b></td>" +
            						"<td><b>Price</b></td>" +
            						"<td><b>Link to Page</b></td>" +
            						"</tr> ";
            				for (Seller s : sellers) 
            				{
            					JSONObject itemJson = new JSONObject();
            					            					
            					itemJson.put("id", Double.toString(s.getKey().getId())) ;
            					itemJson.put("item", s.getItem()) ;
            					itemJson.put("price", s.getPrice()) ;
            					itemJson.put("url", s.getUrl()) ;
            					itemsArray.put(itemJson);
            					
            					resp = resp + " <tr> " +
            							"<td> " + s.getKey().getId() + "</td>" +
            							"<td> " + s.getItem() + "</td>" +
            							"<td> " + s.getPrice() + "</td>" +
            							"<td> <a href = " + s.getUrl() + ">" + s.getUrl() + "</a> </td>" +
            							"</tr> ";
            				}
            			itemsJson.put("items", itemsArray);
            			resp += "</table>";
            			resp += itemsJson.toString();
            			
            			}		
            		}
            	  }   
            	  catch(Exception ex)
            	  {
            		  resp = ex.toString() ;
            	  }
            	  finally
          	  {
          		  queryUser.closeAll();
          	  }
            	resp = "<a href=" + userService.createLogoutURL(this.getReference().toUri().toString()) + ">Sign Out</a></p>" + userName + resp;
            
            	  return resp;
//               return "Welcome " + "user" + this.userName + resp;   
            }
        }
        
        this.getResponse().redirectPermanent(userService.createLoginURL(this.getReference().toUri().toString() ));
        this.setStatus(Status.CLIENT_ERROR_FORBIDDEN, "hello");
       return "<a href=" + userService.createLoginURL(this.getReference().toUri().toString()) + ">Sign in</a> to enter item.</p>" + userName;
	}
	
	@Post
	public String createItem()
	{
		String s = new String();
		s ="hello";
		return s;
	}
	
//    @Get
//    public String represent() {
//        return "hello, world (from the cloud!)";
//    }

}