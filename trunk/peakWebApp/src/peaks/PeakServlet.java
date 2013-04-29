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
public class PeakServlet extends HttpServlet {

	public void doPost (HttpServletRequest request, HttpServletResponse response)throws IOException {
		System.out.println("TP Peak Servelete java");
		String regionSel = request.getParameter("region");

		// this is how we will talk to the database
		Connection c = null;

		// process the request by getting all of the peak names and adding them to the request
		try {

			// set up the connection
			DriverManager.registerDriver(new AppEngineDriver());
			c = DriverManager.getConnection("jdbc:google:rdbms://peak-database:peak-database/peak");

			// execute a query that will obtain all of the peaks
			String statement = "select NAME,ELEV,DIFF,MAP from PEAK where REGION = \'" + regionSel +"\'" ;
			PreparedStatement stmt = c.prepareStatement (statement);
			ResultSet rs = stmt.executeQuery ();

			// store all of the peaks into a list
			ArrayList <String> peakName = new ArrayList <String> ();
			ArrayList <Integer> peakElev = new ArrayList <Integer> ();
			ArrayList <Integer> peakDiff = new ArrayList <Integer> ();
			ArrayList <String> peakMap = new ArrayList <String> ();			
			
			
			while (rs.next ()) {
				peakName.add (rs.getString (1));
				peakElev.add(rs.getInt(2));
				peakDiff.add(rs.getInt(3));
				peakMap.add(rs.getString(4));
			}			

			// close the connection
			c.close ();

			// augment the request by adding the list of regions to it
			request.setAttribute ("peaks", peakName);
			request.setAttribute("elev", peakElev);
			request.setAttribute("diff", peakDiff);
			request.setAttribute("map", peakMap);
			request.setAttribute("regionSel", regionSel);
			System.out.println("Result set ::" + peakName);

			// and forward the request to the "showregions.jsp" page for display
	    		ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher ("/showPeaks.jsp");
			rd.forward (request, response);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}	
	}
}
