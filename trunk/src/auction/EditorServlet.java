package auction;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.BinaryFunctor;
import utils.Functional;
import utils.UnaryFunctor;

import auction.datastore.Email;
import auction.datastore.WatchList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class EditorServlet extends HttpServlet {

	BinaryFunctor<StringBuilder,StringBuilder,StringBuilder> concatBuilders = new BinaryFunctor<StringBuilder,StringBuilder,StringBuilder>(){
		public StringBuilder apply(StringBuilder k1, StringBuilder k2){
			return k1.append(k2);
		}
	};
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Email currentUser = null;
        if (user != null) {
        	final PersistenceManager pm = PMF.get().getPersistenceManager();
        	try{
        	pm.makePersistent(new Item("Item2",KeyFactory.stringToKey("aglub19hcHBfaWRyCwsSBUVtYWlsGAUM")));
            resp.setContentType("text/HTML");
            resp.getWriter().println("Hello, " + user.getNickname()+"<br>");
            
            String newItem = req.getParameter("item"),query;
            
            query = "select from " + Email.class.getName()+ " where email == \""+user.getEmail()+"\"";
            List<Email> users = (List<Email>)pm.newQuery(query).execute();
            if(users.isEmpty()){
            	resp.getWriter().println("This is your first time here, you're in our DB now.");
            	Email id = new Email(user.getEmail());
                pm.makePersistent(id);
                currentUser = id;
            } else{ resp.getWriter().println("Welcome back!"); currentUser = users.get(0); }
            if(newItem!=null){
            	try{
	            	Key itemKey = KeyFactory.stringToKey(newItem);
	            	Entity item = datastoreService.get(itemKey);
	            	if(item.getKind().equals("Item")){
	            		query = "select key from "+WatchList.class.getName()+" where buyer == "+KeyFactory.keyToString(currentUser.getKey())+" && item == "+KeyFactory.keyToString(itemKey);
	            		List<WatchList> watchList = (List<WatchList>)pm.newQuery(query).execute();
	            		if(watchList.isEmpty()){
	            			pm.makePersistent(new WatchList(currentUser.getKey(),itemKey));
	            			resp.getWriter().println("<br>Added new item to watchlist.");
	            		}else{ resp.getWriter().println("<br>Item already on watchlist.");}
	            	} else{resp.getWriter().println("<br>Bad Item Request");}
            	}catch(IllegalArgumentException e){resp.getWriter().println("<br>ERROR! Item does not exist!");}
            	catch(EntityNotFoundException e){resp.getWriter().println("<br>ERROR! Item does not exist!");}
            }
            query = "select from "+WatchList.class.getName()+" where buyer == "+KeyFactory.keyToString(currentUser.getKey());
            List<WatchList> watchList = (List<WatchList>)pm.newQuery(query).execute();
            
            UnaryFunctor<WatchList,StringBuilder> watchListToStringBuilder = new UnaryFunctor<WatchList,StringBuilder>(){
        		public StringBuilder apply(WatchList wl){
        			String query = "select from "+Item.class.getName()+" where key == "+KeyFactory.keyToString(wl.getItem());
                	Item item = ((List<Item>)pm.newQuery(query).execute()).get(0);
                	query = "select from "+auction.datastore.Email.class.getName()+" where key == "+KeyFactory.keyToString(item.getSeller());
                	Email seller = ((List<Email>)pm.newQuery(query).execute()).get(0); 
        			StringBuilder sb = new StringBuilder();
        			sb.append("<br>").append(item.getName()).append(" From: ").append(seller.getEmail())
        			.append("<form name=\"bidForm\"  method=\"post\"  action=\"/bid?item=")
        			.append(KeyFactory.keyToString(item.getKey())).append("\">\n<div> Bid: <input type=\"text\" name=\"bid\" id=\"bid\"></input></div><div><input type=\"submit\" value=\"Bid\" /></div></form>");

        			return sb;
        		}
        	};
        	if(!watchList.isEmpty()){
	        	resp.getWriter().println("<br> Watchlist: ");
	            resp.getWriter().println(Functional.mapReduce(watchList,watchListToStringBuilder,concatBuilders));
        	}

            resp.getWriter().println("<a href=\""+userService.createLogoutURL(req.getRequestURI()) +"\">Log Off</a>");
        	}finally{ pm.close();}
        } else {//user == null - LOG IN!
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
    }
}