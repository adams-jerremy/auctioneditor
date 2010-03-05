package auction;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auction.datastore.Email;
import auction.datastore.Seller;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoggedInServlet extends HttpServlet{
	private static final Logger log = Logger.getLogger(LoggedInServlet.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		String item = req.getParameter("item");
		Date date = new Date();
		String url = req.getParameter("url");
		String price = req.getParameter("price");

		PersistenceManager pmUser = auction.PMF.get().getPersistenceManager();
		Query query = pmUser.newQuery(auction.datastore.Email.class);
		query.setFilter("email == uemail");
		query.declareParameters("String uemail");

		Email currentUser = null;

		List<auction.datastore.Email> users = (List<auction.datastore.Email>)query.execute(user.getEmail());

		if(users.isEmpty()){
			// UserInfo newUser = new UserInfo(user);
			Email newUser = new Email(user.getEmail());
			// PersistenceManager pmU = PMF.get().getPersistenceManager();
			try{
				pmUser.makePersistent(newUser);
				currentUser = newUser;
			}finally{
				pmUser.close();
			}
		}
		else{
			currentUser = users.get(0);
		}

		Seller greeting = new Seller(currentUser.getKey(), item, date, url,
				price);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(greeting);
		}finally{
			pm.close();
			resp.sendRedirect("/enterItem.jsp");
		}

	}
}