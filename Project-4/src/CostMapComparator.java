import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class CostMapComparator implements Comparator<ArrayList<Integer>> {

	Map<ArrayList<Integer> , Number> base;
	public CostMapComparator(Map<ArrayList<Integer> , Number> base) {
		this.base = base;
	}

	public int compare(ArrayList<Integer> a, ArrayList<Integer> b) {
		if (base.get(a).doubleValue() >= base.get(b).doubleValue()) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}


}

