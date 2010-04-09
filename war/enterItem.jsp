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

function sendItemToServer() {
	if(checkForm())
	{
		xmlhttpLogin=getXmlHttpObject();
		if (xmlhttp === null)
	  	{
	  		alert ("Your browser does not support AJAX!");
	  		return;
	  	}
	  	var url="/sign";
	  	xmlhttpLogin.onreadystatechange=loginStateChanged;
	  	xmlhttpLogin.open("POST",url,true);
	  	xmlhttpLogin.send(null);		
	}		
}
function loginStateChanged() {
	function stateChanged()
	{
		if (xmlhttpLogin.readyState==4)
	  	{
			document.getElementById("dump").innerHTML=xmlhttpLogin.responseText;
	  	}
	}
		
}
function checkForm() {
	//showItems();
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
	if(elem.value.length === 0){
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

function deleteRow(tableID) {
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

function getXmlHttpObject() {
	if (window.XMLHttpRequest)
	  {
	  // code for IE7+, Firefox, Chrome, Opera, Safari
	  return new XMLHttpRequest();
	  }
	if (window.ActiveXObject)
	  {
	  // code for IE6, IE5
	  return new ActiveXObject("Microsoft.XMLHTTP");
	  }
	return null;
}

function showItems() {
	xmlhttp=getXmlHttpObject();
	if (xmlhttp==null)
  	{
  		alert ("Your browser does not support AJAX!");
  		return;
  	}
  	var url="/getSellerItems";
  	xmlhttp.onreadystatechange=stateChanged;
  	xmlhttp.open("POST",url,true);
  	xmlhttp.send(null);
	
}

function stateChanged() {
	if (xmlhttp.readyState==4)
  	{
  		document.getElementById("sellerItems").innerHTML=xmlhttp.responseText;
  		
  	}
}

function sendFile() {
	return true;
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

<iframe id="formSubmit" name="formSubmit" height="0" width="0"  frameborder="0" onload="showItems()"></iframe>
<form name="itemForm"  method="post" target="formSubmit" onsubmit="sendItemToServer()" action="/sign"> 
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
<div id="dump"></div>

<FORM method="post" action="/upload" target="uploadFile" onsubmit="return sendFile()" enctype="multipart/form-data">
<INPUT TYPE="file" name="uFile" id="uFile">
<div><input type="submit" value="Submit file" /> </div>
</FORM>
<iframe id="uploadFile" name="uploadFile" height="0" width="0" frameborder="0"></iframe>
<div id="sellerItems" > Seller Items: </div>
<%

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

