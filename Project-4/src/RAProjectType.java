import java.util.ArrayList;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class RAProjectType implements IRAType {
	private String type;
	private String value;
	private IRAType _next;
	private ArrayList <Expression> selectExprs;
	private ReturnJoin _outputInfo;
	/**
	 * @param _iraType
	 */
	public RAProjectType(IRAType _iraType) {
		this.type = "RA_PROJECT_TYPE";
		this._next = _iraType;
	}

	/* (non-Javadoc)
	 * @see IRAType#getType()
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see IRAType#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the selectExprs
	 */
	public ArrayList<Expression> getSelectExprs() {
		return selectExprs;
	}

	/**
	 * @param selectExprs the selectExprs to set
	 */
	public void setSelectExprs(ArrayList<Expression> selectExprs) {
		this.selectExprs = selectExprs;
	}

	/**
	 * @return the _next
	 */
	public IRAType getNext() {
		return _next;
	}

	/**
	 * @param _next the _next to set
	 */
	public void setNext(IRAType _next) {
		this._next = _next;
	}

}
