<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="java.util.List"%>
<%@ page import="auction.Seller"%>

<html>
<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>Hello, <%= user.getNickname() %>! (You can <a
	href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign
out</a>.)</p>

<form action="/sign" method="post">
<!-- <div><textarea name="content" rows="3" cols="60"></textarea></div> -->
<div> Item name: <input type="text" name="item"></input></div>
<div> Url for Item: <input type="text" name="url"></input></div>
<div> Price for Item: <input type="text" name="price"></input></div>

<div><input type="submit" value="Submit Item" /></div>
</form>

<% 
	String query = "select from " + auction.Seller.class.getName();///+ " where seller == " + user.getUserId().toString();
	PersistenceManager pm = auction.PMF.get().getPersistenceManager();
	List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
	if (!sellers.isEmpty()) {
%>
	<table>
	<%
		for (Seller s : sellers) {
			%> <tr> <%
			%> <td> <%= s.getItem()  %> </td><%
			%> <td> <%= s.getPrice()  %> </td> <%
			%> <td> <%= s.getUrl()  %> </td> <%
			%> </tr> <%
		}
	}
	%>
	</table>
<%
    } else {
%>
<p>Hello! <a
	href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign
in</a> to enter item.</p>
<%
    }
%>

</body>
</html>
