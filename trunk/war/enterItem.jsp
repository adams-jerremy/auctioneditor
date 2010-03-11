<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="java.util.List"%>
<%@ page import="auction.datastore.Seller"%>
<%@ page import="auction.datastore.Email"%>
<%@ page import="javax.jdo.Query"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>


<html>
<body>

<script type='text/javascript'>

function checkForm() {
	var numItems = document.getElementById('numItems');
	var headRow = 1;
	for( var i=1; i< headRow + numItems.getAttribute('value'); ++i)
	{
	var item = document.getElementById('item' + i);
	var url = document.getElementById('url' + i);
	var price = document.getElementById('price' + i);

	if(notEmpty(item, "Please enter item name of item " + i) && notEmpty(price, "Please enter price for item " + i))
	{
		if(isAlphanumeric(item, "Please enter only Numbers and Letters for name of item "+ i))
		{
			if(isNumeric(price, "Please enter a valid price " + i)){
				
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	else
	{
		return false;
	}
	}
	return true;

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

function addRow(tableID) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;

    var numItems = document.getElementById('numItems');
	//var headRow = 1;
   // for(var i=rowCount-1; i <= numItems + headRow; i++ )
    //{
	var row = table.insertRow(rowCount);
	
	var cell1 = row.insertCell(0);
	var elem = document.createElement("input");
	elem.setAttribute("id", "item" + rowCount);
	elem.setAttribute("name", "item" + rowCount);
	var prevItem =  document.getElementById('item' + (rowCount-1));
    elem.setAttribute("value", prevItem.getAttribute("value")); 
    elem.setAttribute("alt", "item" + rowCount); 
    elem.setAttribute("type", "text"); 
    cell1.appendChild(elem);

	var cell2 = row.insertCell(1);
	var elem = document.createElement("input");
	var prevURL =  document.getElementById('url' + (rowCount-1));
	elem.setAttribute("id", "url" + rowCount); 
	elem.setAttribute("name", "url" + rowCount); 
    elem.setAttribute("value", prevURL.getAttribute("value")); 
    elem.setAttribute("alt", "url" + rowCount); 
    elem.setAttribute("type", "text"); 
    cell2.appendChild(elem);

	var cell3 = row.insertCell(2);
	var elem = document.createElement("input");
	var prevPrice =  document.getElementById('price' + (rowCount-1));
	elem.setAttribute("id", "price" + rowCount);
	elem.setAttribute("name", "price" + rowCount);  
    elem.setAttribute("value",  prevPrice.getAttribute("value")); 
    elem.setAttribute("alt", "price" + rowCount); 
    elem.setAttribute("type", "text"); 
    cell3.appendChild(elem);

    numItems.setAttribute("value",(rowCount));
    
}

function deleteRow(tableID) 
{
	try 
	{
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		if(rowCount > 2)
		{
			table.deleteRow(rowCount-1);
			rowCount--;
		    var numItems = document.getElementById('numItems');
		    numItems.setAttribute("value",(rowCount-1));
		}
	}
	catch(e)
	{
		alert(e);
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

<INPUT type="button" value="Add Row" onclick="addRow('addTable')" />
<INPUT type="button" value="Delete Row" onclick="deleteRow('addTable')" />
<input type="hidden" name="numItems" id="numItems" value="1" />


<table id="addTable">
	<tr>
		<td> Item name </td>
		<td> Url for Item </td>
		<td> Price for Item </td>
	</tr>
	<tr>
		<td> <input type="text" name="item1" id="item1"></input></td>
		<td> <input type="text" name="url1" id="url1"></input></td>
		<td> <input type="text" name="price1" id="price1"></input></td>
	</tr>
</table>
<div><input type="submit" value="Submit Item" /> </div>
</form>

<% 
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
//			Query query = pm.newQuery("select id from" + auction.Seller.class);
			query.setFilter("seller == ukey");
			query.declareParameters("String ukey");
		String qry = "select from " + Seller.class.getName() + " where seller == " + KeyFactory.keyToString(currentEmail.getKey());
			//List<auction.Seller> sellers = (List<auction.Seller>) pm.newQuery(query).execute();
List<Seller> sellers = (List<Seller>)pm.newQuery(qry).execute();
			//List<auction.Seller> sellers = (List<auction.Seller>) query.execute(KeyFactory.keyToString(currentEmail.getKey()));
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

