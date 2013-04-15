import java.util.ArrayList;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class ReturnJoin  {

	
	private ArrayList<AttribJoin> joinOutAttribts;
	private String outputFile;
	
	/**
	 * @param joinOutAttribts
	 * @param outputFile
	 */
	public ReturnJoin(ArrayList<AttribJoin> joinOutAttribts, String outputFile) {
		this.joinOutAttribts = joinOutAttribts;
		this.outputFile = outputFile;
	}

	/**
	 * @return the joinOutAttribts
	 */
	public ArrayList<AttribJoin> getJoinOutAttribts() {
		return joinOutAttribts;
	}

	/**
	 * @return the outputFile
	 */
	public String getOutputFile() {
		return outputFile;
	}
	
	
}
