package peaks;

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

@SuppressWarnings("serial")
public class ClimberServlet extends HttpServlet {

	public void doPost (HttpServletRequest request, HttpServletResponse response)throws IOException {
		System.out.println("TP Climber Servelete java");
		String peakSel = request.getParameter("peak");

		// this is how we will talk to the database
		Connection c = null;

		// process the request by getting all of the peak names and adding them to the request
		try {

			// set up the connection
			DriverManager.registerDriver(new AppEngineDriver());
			c = DriverManager.getConnection("jdbc:google:rdbms://peak-database:peak-database/peak");

			// execute a query that will obtain all of the peaks
			
			String statement =	"select P.NAME,C.WHEN_CLIMBED from CLIMBED C, PARTICIPATED P where C.PEAK =  \'" + peakSel +"\'" +
							"and  C.TRIP_ID = P.TRIP_ID " ;
			
			PreparedStatement stmt = c.prepareStatement (statement);
			ResultSet rs = stmt.executeQuery ();

			// store all of the peaks into a list
			ArrayList <String> climberName = new ArrayList <String> ();
			ArrayList <String> climberDate = new ArrayList <String> ();
			
			while (rs.next ()) {
				
				//String temp = rs.getString (1);
				String temp = new String((rs.getString (1)).substring(0, 1) + (((rs.getString (1))).substring(1)).toLowerCase());				
				climberName.add (temp);
				climberDate.add( rs.getString(2));
			}			

			// close the connection
			c.close ();

			// augment the request by adding the list of regions to it
			request.setAttribute ("climber", climberName);
			request.setAttribute("peak", peakSel);
			request.setAttribute("date", climberDate);
			
			System.out.println("Result set ::" + climberName);

			// and forward the request to the "showregions.jsp" page for display
	    		ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher ("/showClimber.jsp");
			rd.forward (request, response);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}	
	}
}
