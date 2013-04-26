package tp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class TpServlet extends HttpServlet {
	public void doGet (HttpServletRequest request, HttpServletResponse response)throws IOException {

		// make sure that the user has been authenticated
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		// if not, send them to the login page
		if (user == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
//			System.out.println("User ::" + null);
//			System.out.println("Request ::" + request.getQueryString());

			// if so, then add them to the session
		} 
		else {
//			
//			String uEmail = user.getEmail();
//			System.out.println("Email : " + uEmail);
//			String domain = uEmail.substring(uEmail.indexOf('@')+1);
//			if(domain.equals("gmail.com")){
				request.getSession ().setAttribute ("user", user.getEmail ());
//				System.out.println("User ::" + user.getEmail());
//			}
//			else
//				response.sendRedirect(userService.createLoginURL(request.getRequestURI()));

		}
		
		//System.out.println("Response ::" + response.toString());
		// this is how we will talk to the database
		Connection c = null;

		// process the request by getting all of the peak names and adding them to the request
		try {

			// set up the connection
			DriverManager.registerDriver(new AppEngineDriver());
			c = DriverManager.getConnection("jdbc:google:rdbms://your:peak/peak-database:peak-database");

			// execute a query that will obtain all of the peaks
			String statement = "select distinct REGION from PEAK";
			PreparedStatement stmt = c.prepareStatement (statement);
			ResultSet rs = stmt.executeQuery ();

			// store all of the peaks into a list
			ArrayList <String> myList = new ArrayList <String> ();
			while (rs.next ()) {
				myList.add (rs.getString (1));
			}			

			// close the connection
			c.close ();

			// augment the request by adding the list of regions to it
			request.setAttribute ("regions", myList);
			
			System.out.println("Result set ::" + myList);
			
			// and forward the request to the "showregions.jsp" page for display
	    		ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher ("/showregions.jsp");
			rd.forward (request, response);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}		
		
	}
}
