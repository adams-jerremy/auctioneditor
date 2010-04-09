package auction;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.restlet.resource.ServerResource;

import auction.datastore.Email;
import auction.datastore.Seller;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Resource which has only one representation.
 * 
 */
public class SellerResource extends ServerResource {

	String userName;
	
	@Override  
    protected void doInit() throws ResourceException {  
        // Get the "itemName" attribute value taken from the URI template  
        // /items/{itemName}.  
        this.userName = (String) getRequest().getAttributes().get("seller");  
  
        // Get the item directly from the "persistence layer".  
       // this.item = getItems().get(this.userName);  
  
      //  setExisting(this.item != null);  
    }  
  
	
    @Get
    public String represent(){//Request request, Response response) {
    	   UserService userService = UserServiceFactory.getUserService();
           User user = userService.getCurrentUser();
           
           if (user != null) {
        	   if( user.getEmail().split("@")[0].compareTo(this.userName) == 0 )
               {
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
//               			Query query = pm.newQuery("select id from" + auction.Seller.class);
               			query.setFilter("seller == ukey");
               			query.declareParameters("String ukey");
               		String qry = "select from " + Seller.class.getName() + " where seller == " + KeyFactory.keyToString(currentEmail.getKey());
               			//List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
               List<Seller> sellers = (List<Seller>)pm.newQuery(qry).execute();
               			//List<auction.Seller> sellers = (List<auction.Seller>) query.execute(KeyFactory.keyToString(currentEmail.getKey()));
               
               		
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
               					resp = resp + " <tr> " +
               							"<td> " + s.getKey().getId() + "</td>" +
               							"<td> " + s.getItem() + "</td>" +
               							"<td> " + s.getPrice() + "</td>" +
               							"<td> <a href = " + s.getUrl() + ">" + s.getUrl() + "</a> </td>" +
               							"</tr> ";
               				}
               			resp += "</table>";
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
               	resp = "<a href=" + userService.createLogoutURL(this.getReference().toUri().toString()) + ">Sign Out</a></p>" + resp;
               	  return resp;
//                  return "Welcome " + "user" + this.userName + resp;   
               }
           }
          
           this.getResponse().redirectPermanent(userService.createLoginURL(this.getReference().toUri().toString() ));
           this.setStatus(Status.CLIENT_ERROR_FORBIDDEN, "hello");
          return "<a href=" + userService.createLoginURL(this.getReference().toUri().toString()) + ">Sign in</a> to enter item.</p>";
          
//          DomRepresentation representation = new DomRepresentation(  
//                  MediaType.TEXT_XML);  
//          // Generate a DOM document representing the item.  
//          Document d = representation.getDocument();  

//          String item;
//          Element eltItem = d.createElement("seller");  
//          d.appendChild(eltItem);  
//          Element eltName = d.createElement("name");  
//          eltName.appendChild(d.createTextNode(item.getName()));  
//          eltItem.appendChild(eltName);  
//
//          Element eltDescription = d.createElement("description");  
//          eltDescription.appendChild(d.createTextNode(item.getDescription()));  
//          eltItem.appendChild(eltDescription);  
//
//          d.normalizeDocument();  
//
//          // Returns the XML representation of this document.  
//          return representation; 
           
//        return "hello, world from " + this.userName;
    }

}