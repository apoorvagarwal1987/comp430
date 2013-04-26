
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.rdbms.AppEngineDriver" %>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<body>
<form action="/peak" method="GET">
<%	
	// get the user
        String user = (String) session.getAttribute ("user");
        if (user == null) {
%>
                <p>You are not logged in.</p>
<%
        } else {
%>
		<p>Hello, <%= user %>.</p>
		<p>Hello!  The time is now <%= new java.util.Date() %></p>
		<p>The Climber for the peak:</p>
		<ol>
<%
		ArrayList <String> myList = (ArrayList <String>) request.getAttribute ("climber");
		for (String s : myList) {
			out.println ("<li>" + s + "</li>");
		}
		out.println ("</ol>");
	}	
%>


<P><INPUT TYPE = SUBMIT VALUE = HOME>
</form>


</body>
</html>
