import java.util.ArrayList;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class ResultValidQuery {
	boolean result;
	ArrayList<ResultValue> selTypes;
	/**
	 * @param result
	 * @param selTypes
	 */
	public ResultValidQuery(boolean result, ArrayList<ResultValue> selTypes) {
		this.result = result;
		this.selTypes = selTypes;
	}
	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}
	/**
	 * @return the selTypes
	 */
	public ArrayList<ResultValue> getSelTypes() {
		return selTypes;
	}
	
}
