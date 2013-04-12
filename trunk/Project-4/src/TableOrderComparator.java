import java.util.Comparator;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class TableOrderComparator implements Comparator<RATableType> {
	
	public int compare(RATableType o1, RATableType o2) {
		if(o1.getTupleCount() > o2.getTupleCount())
			return 1;
		else if (o1.getTupleCount() == o2.getTupleCount())
			return 0;
		else
			return -1;
	}
}