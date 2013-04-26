
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
<form action="/tp" method="GET">
<%	
	// get the user
        String user = (String) session.getAttribute ("user");
        if (user == null) {
%>
                <p>You are not logged in.</p>
<%
        } else {
        
        String peakName = (String) request.getAttribute("peak");
%>
		<p>Hello, <%= user %>.</p>
		<p>Hello!  The time is now <%= new java.util.Date() %></p>		  
		<ol>
<%
		ArrayList <String> myList = (ArrayList <String>) request.getAttribute ("climber");
		ArrayList <String> climberDate = (ArrayList <String>) request.getAttribute ("date");
		int pos  = 0;
		for (String s : myList) {
			out.println ("<li>" + s + "  has climbed on "+ climberDate.get(pos)  + "</li>");
			pos++;
		}
		out.println ("</ol>");
	}	
%>


<P><INPUT TYPE = SUBMIT VALUE = HOME>
</form>


</body>
</html>
