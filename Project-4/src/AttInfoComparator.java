import java.util.Comparator;

/**
 * 
 */

/**
 * @author apoorvagarwal
 * The class is used to do the 
 * sorting of the attributes in the table
 */
public class AttInfoComparator implements Comparator<AttInfo>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(AttInfo o1, AttInfo o2) {
		if(o1.getAttSequenceNumber() > o2.getAttSequenceNumber())
			return 1;
		else if (o1.getAttSequenceNumber() == o2.getAttSequenceNumber())
			return 0;
		else
			return -1;
	}
	

}
