
/**
 * This little class holds the catalog info about an attribute.
 * 
 * Everything is pretty self explanatory, except for the "seqNumber"
 * field, which stores whether this is the first, second, third, etc., attribute
 * in the table to which this attribute belongs.
 */
class AttInfo {
 
  private int valueCount;
  private String dataType;
  private int seqNumber;
  private String attName;
  
  public AttInfo (int numDistinctVals, String myType, int whichAtt, String attName) {
    valueCount = numDistinctVals;
    dataType = myType;
    seqNumber = whichAtt;
    this.attName = attName;
  }
  
  public int getNumDistinctVals () {
    return valueCount; 
  }
  
  public String getDataType () {
    return dataType; 
  }
  
  public int getAttSequenceNumber () {
    return seqNumber; 
  }
  
   /**
    * @return the attName
    */
  public String getAttName() {
	return attName;
  }

	String print () {
	    return "attName:"+attName + " vals: " + valueCount + "; type: " + dataType + "; attnum: " + seqNumber;  
	  }
}