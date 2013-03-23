
public class ResultValue {
	String type;
	boolean result;
	
	public ResultValue(String type, boolean result){
		this.type = type;
		this.result = result;			
	}

	public String getType() {
		return type;
	}

	public boolean isResult() {
		return result;
	}	
}
