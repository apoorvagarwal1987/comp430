
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

	public boolean isResult() {
		return result;
	}	
	
	public void print (){
		System.out.println(type + "   " + result);
	}
}
