/**
 * 
 */

/**
 * @author ApoorvAgarwal
 *
 */
public class StringCompatibility {
	
	public ResultValue compatibility (ResultValue _resValue1, ResultValue _resValue2, String type){
		
		//Result value of the right expression is String type
		if(_resValue2.getType()==0){
			
			/*switch(type){
		  	case "minus":
		  	case "times":
		  	case "divided by"  : 
		  		return (new ResultValue(-1, false));
*/
			if(type.equals("minus")){
				return (new ResultValue(-1, false));
			}		
			
			if(type.equals("times")){
				return (new ResultValue(-1, false));
			}
			
			if(type.equals("divided by")){
				return (new ResultValue(-1, false));
			}
		}
		//Result value of the right expression is Integer type
		else{
			for(String incompatibility : Expression.incompatibleTypes)
				  if(type.equals(incompatibility)){
					  return (new ResultValue(-1, false));
				  }
		}		
		return (new ResultValue(0, true));	
	}
}
