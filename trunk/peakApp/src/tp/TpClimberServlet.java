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

@SuppressWarnings("serial")
public class TpClimberServlet extends HttpServlet {

	public void doGet (HttpServletRequest request, HttpServletResponse response)throws IOException {
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
			
			String statement =	"select P.NAME from CLIMBED C, PARTICIPATED P where C.PEAK =  \'" + peakSel +"\'" +
							"and  C.TRIP_ID = P.TRIP_ID " ;
			
			PreparedStatement stmt = c.prepareStatement (statement);
			ResultSet rs = stmt.executeQuery ();

			// store all of the peaks into a list
			ArrayList <String> climberName = new ArrayList <String> ();
			
			while (rs.next ()) {
				climberName.add (rs.getString (1));
			}			

			// close the connection
			c.close ();

			// augment the request by adding the list of regions to it
			request.setAttribute ("climber", climberName);
;

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
