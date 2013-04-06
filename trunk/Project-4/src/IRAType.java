/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public interface IRAType {
	
	public String getType();
	public void setType(String type);
	
/*	public IRAType getNext();
	public void setNext(IRAType _next);
	
*/
	
	public IRAType getPrevious();
	public void setPrevious(IRAType _previous);
	
}
