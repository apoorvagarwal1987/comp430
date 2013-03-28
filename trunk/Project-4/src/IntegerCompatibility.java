/**
 * 
 */

/**
 * @author ApoorvAgarwal
 *
 */
public class IntegerCompatibility {
	public ResultValue compatibility (ResultValue _resValue1, ResultValue _resValue2, String type){
		//Result value of the right expression is String type
		if(_resValue2.getType()==0){
			for(String incompatibility : Expression.incompatibleTypes)
				  if(type.equals(incompatibility)){
					  return (new ResultValue(-1, false));
				  }
		  }		
		return (new ResultValue(_resValue1.getType(), true));		
	}
}
