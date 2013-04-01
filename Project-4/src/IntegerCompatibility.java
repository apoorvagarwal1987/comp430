/**
 * 
 */

/**
 * @author ApoorvAgarwal
 * The class is used to check the conformity between the 
 * two result values passed depending upon the fact that the 
 * caller has an INT/FLOAT type
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
		int rettype = (_resValue1.getType() > _resValue2.getType() ? _resValue1.getType() : _resValue2.getType());
		return (new ResultValue(rettype, true));		
	}
}
