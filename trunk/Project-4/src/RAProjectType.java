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
	private RASelectType _raSelect;
	private ArrayList <Expression> selectExprs;
	/**
	 * @param _raSelect
	 */
	public RAProjectType(RASelectType _raSelect) {
		this.type = "RA_PROJECT_TYPE";
		this._raSelect = _raSelect;
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
	 * @return the _raSelect
	 */
	public RASelectType get_raSelect() {
		return _raSelect;
	}

	/**
	 * @param _raSelect the _raSelect to set
	 */
	public void set_raSelect(RASelectType _raSelect) {
		this._raSelect = _raSelect;
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

}
