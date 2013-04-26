
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
<form action="/climber" method="GET">

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
		<p>The Peaks in the database are:</p>
		<ol>
<%
		ArrayList <String> myList = (ArrayList <String>) request.getAttribute ("peaks");
		ArrayList <Integer> peakElev = (ArrayList <Integer>) request.getAttribute ("elev");
		ArrayList <Integer> peakDiff = (ArrayList <Integer>) request.getAttribute ("diff");
		ArrayList <String> peakMap = (ArrayList <String>) request.getAttribute ("map");
		
		int pos = 0;
		
		for (String s : myList) {
			out.println ("<li>" + s + "  (The elevation is: "+ peakElev.get(pos)+ " , difficulty level is: " + peakDiff.get(pos)+  
						"  and is located on Map: "+ peakMap.get(pos)+ " )" + "</li>");
			pos++;
		}
		out.println ("</ol>");
	}	
%>

Select Your Peak? <INPUT TYPE=TEXT NAME="peak" SIZE=20><BR>
<P><INPUT TYPE=SUBMIT VALUE= SELECT>
</form>

</body>
</html>
