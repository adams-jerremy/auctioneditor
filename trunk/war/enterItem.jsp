<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="java.util.List"%>
<%@ page import="auction.Seller"%>
<%@ page import="auction.Email"%>
<%@ page import="javax.jdo.Query"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>


<html>
<body>

<script type='text/javascript'>

function checkForm() {
	var item = document.getElementById('item');
	var url = document.getElementById('url');
	var price = document.getElementById('price');

	if(notEmpty(item, "Please enter item name") && notEmpty(price, "Please enter price for item"))
	{
		if(isAlphanumeric(item, "Please enter only Numbers and Letters for item name"))
		{
			if(isNumeric(price, "Please enter a valid price")){
				return true;
			}
		}
	}
	return false;

}

function isAlphanumeric(elem, helperMsg){
	var alphaExp = /^[0-9a-zA-Z]+$/;
	if(elem.value.match(alphaExp)){
		return true;
	}else{
		alert(helperMsg);
		elem.focus();
		return false;
	}
}
function notEmpty(elem, helperMsg){
	if(elem.value.length == 0){
		alert(helperMsg);
		elem.focus(); // set the focus to this input
		return false;
	}
	return true;
}

function isNumeric(elem, helperMsg){
	var numericExpression = /^[0-9]+.?[0-9]+$/;
	if(elem.value.match(numericExpression)){
		return true;
	}else{
		alert(helperMsg);
		elem.focus();
		return false;
	}
}
</script>

<%
  
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) 
    {
%>
<p>Hello, <%= user.getNickname() %>! (You can <a
	href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign
out</a>.)</p>

<form name="itemForm"  method="post" onsubmit="return checkForm()" action="/sign"> 
<!-- <div><textarea name="content" rows="3" cols="60"></textarea></div> -->
<div> Item name: <input type="text" name="item" id="item"></input></div>
<div> Url for Item: <input type="text" name="url" id="url"></input></div>
<div> Price for Item: <input type="text" name="price" id="price"></input></div>

<div><input type="submit" value="Submit Item" /> </div>
</form>

<% 
	PersistenceManager pmUser = auction.PMF.get().getPersistenceManager();
	Query queryUser = pmUser.newQuery( auction.Email.class);
	queryUser.setFilter("email == uemail");
	queryUser.declareParameters("String uemail");
	  try
	  {
		List<Email> users = (List<Email>) queryUser.execute(user.getEmail());
		Email currentUser = null;
	
		if( !users.isEmpty() )
		{
			currentUser = users.get(0);	
			//String query = "select from " + auction.Seller.class.getName()+ " where seller == " + user.getUserId();
			PersistenceManager pm = auction.PMF.get().getPersistenceManager();
			Query query = pm.newQuery( auction.Seller.class);
//			Query query = pm.newQuery("select id from" + auction.Seller.class);
			query.setFilter("seller == ukey");
			query.declareParameters("String ukey");
		
			//List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
			List<auction.Seller> sellers = (List<auction.Seller>) query.execute(KeyFactory.keyToString(currentUser.getKey()));
			if (!sellers.isEmpty()) 
			{ 
%>

			<table>
			<tr>
			<td><b>Item</b></td>
			<td><b>Price</b></td>
			<td><b>Link to Page</b></td>
			</tr>
			<%
				for (Seller s : sellers) 
				{
					%> <tr> <%
					%> <td> <%= s.getItem()  %> </td><%
					%> <td> <%= s.getPrice()  %> </td> <%
					%> <td> <a href = "<%= s.getUrl()  %>" ><%= s.getUrl()  %></a> </td> <%
					%> </tr> <%
				}
			%>
			</table>


			<!-- IFRAME SRC="http://z.cs.utexas.edu/users/varunjn/AustinBikeAuction/bike1.html" TITLE="My other page" NAME="otherpage" FRAMEBORDER="0" WIDTH="50%" HEIGHT="100">Alternate content for non-supporting browsers, probably a link to the same info</IFRAME-->
		<%
			}		
		}
	  }
	  finally
	  {
		  queryUser.closeAll();
	  }
	}
    else 
    {
%>
<p>Hello! <a
	href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign
in</a> to enter item.</p>
<%
    }
%>

</body>
</html>

