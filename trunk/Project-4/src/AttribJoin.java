/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class AttribJoin {

	
	private AttInfo _attinfo;
	private int sequenceNumber;
	
	/**
	 * @param attrib
	 * @param i
	 */
	public AttribJoin(AttInfo attrib, int i) {
		
		this._attinfo = attrib;
		this.sequenceNumber = i;		
	}

	/**
	 * @return the _attinfo
	 */
	public AttInfo get_attinfo() {
		return _attinfo;
	}

	/**
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
}
