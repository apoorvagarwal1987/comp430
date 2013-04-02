
public class ResultValue {
	int type;
	boolean result;
	
	public ResultValue(int type, boolean result){
		this.type = type;
		this.result = result;			
	}

	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public boolean isResult() {
		return result;
	}	
	
	public void print (){
		System.out.println(type + "   " + result);
	}
}
