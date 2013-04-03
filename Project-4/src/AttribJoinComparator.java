import java.util.Comparator;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class AttribJoinComparator implements Comparator<AttribJoin> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(AttribJoin o1, AttribJoin o2) {
		if(o1.getSequenceNumber() > o2.getSequenceNumber())
			return 1;
		else if (o1.getSequenceNumber() == o2.getSequenceNumber())
			return 0;
		else
			return -1;
	}

}
