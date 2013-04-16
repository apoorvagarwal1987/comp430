import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.language.Expr;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class CostingRA {
	
	
	private static ArrayList<Expression> selectExpression ;
	
	static {
		selectExpression = new ArrayList<Expression>();
	}
	
	private static void makeExpression(Expression expression){
        String type = expression.getType().toString();
        if(type.equals("and") || type.equals("or")){
        	makeExpression(expression.getLeftSubexpression());
        	makeExpression(expression.getRightSubexpression());
        }
        else if(CommonMethods.isUnaryOperation(type)){
        	makeExpression(expression.getLeftSubexpression());
        }
        else{
        	selectExpression.add(expression);
        }
	}
	
	
	
	public static ReturnJoin costing (IRAType current){
		
		if(current.getType().equals("RA_JOIN_TYPE")){
			
			IRAType leftNode = ((RAJoinType)current).getLeft();
			IRAType rightNode = ((RAJoinType)current).getRight();
			
			ReturnJoin leftReturn = costing(leftNode);
			ReturnJoin rightReturn = costing(rightNode);
			
			
			double leftTupleCount = leftNode.getTupleCount();
			double rightTupleCount = rightNode.getTupleCount();
			
			ArrayList<AttribJoin> leftAttributes = leftReturn.getJoinOutAttribts();
			ArrayList<AttribJoin> rightAttributes = rightReturn.getJoinOutAttribts();
			Map<String,AttInfo> outAttributes = new HashMap<String, AttInfo>();
			ArrayList<Expression> joinPredicate = ((RAJoinType)current).getSelectionPredicate();
			double tOut = 0 ;
			
			/**
			 * CASE 1: The condition of the cross join
			 */
			
			Collections.sort(leftAttributes,new AttribJoinComparator());
			Collections.sort(rightAttributes, new AttribJoinComparator());
			
			if(joinPredicate.size() == 0){
				tOut = leftTupleCount * rightTupleCount;
				int pos = 1;
				// Walking over all the attributes from the left side of the branch 
				
				ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
				
				for(AttribJoin attInformation : leftAttributes){
					AttInfo cAttInfo = attInformation.getAttinfo();
					String cAttName =cAttInfo.getAttName();
					if(outAttributes.get(cAttName)== null){
						outAttributes.put(cAttName, cAttInfo);
					}
				}
				
				for(AttribJoin attInformation : rightAttributes){
					AttInfo cAttInfo = attInformation.getAttinfo();
					String cAttName = cAttInfo.getAttName();
					if(outAttributes.get(cAttName)== null){
						outAttributes.put(cAttName, cAttInfo);
					}
				}
				
				for(Entry<String, AttInfo> outAttribSet : outAttributes.entrySet()){
					AttInfo value = outAttribSet.getValue();
					joinOutAttribts.add(new AttribJoin(value, pos++));
				}
				
				ReturnJoin outputInfo = new ReturnJoin(joinOutAttribts,"costing");
				current.setTupleCount(tOut);
				return outputInfo;
			}
			
			/**
			 * CASE 2: The condition of the JOIN predicate
			 * 			Handling the cases where the join predicates 
			 */
			else{
				double leftDenom = 1;
				double rightDenom = 1;
				
				for(AttribJoin attInformation : leftAttributes){
					AttInfo lAttInfo = attInformation.getAttinfo();
					String lAttName =""+lAttInfo.getAlias() +"_"+lAttInfo.getAttName();
					outAttributes.put(lAttName, lAttInfo);
				}
				for(AttribJoin attInformation : rightAttributes){
					AttInfo rAttInfo = attInformation.getAttinfo();
					String rAttName =""+rAttInfo.getAlias() +"_"+rAttInfo.getAttName();
					outAttributes.put(rAttName, rAttInfo);
				}			
				
				for(Expression exp : joinPredicate){					
					Expression lExp = exp.getLeftSubexpression();
					Expression rExp = exp.getRightSubexpression();
					String lAttribute = lExp.getValue().replace('.', '_');
					String rAttribute = rExp.getValue().replace('.', '_');
					String lAttNameJoin = null;
					String rAttNameJoin = null;
					for(AttribJoin attInformation : leftAttributes){
						AttInfo cAttInfo = attInformation.getAttinfo();
						String cAttName =""+cAttInfo.getAlias() +"_"+cAttInfo.getAttName();
						if(lAttribute.equals(cAttName)){
							leftDenom = leftDenom * cAttInfo.getOutputCount();
							lAttNameJoin = cAttName;
							break;
						}	
						else if (rAttribute.equals(cAttName)){
							rightDenom = rightDenom * cAttInfo.getOutputCount();
							rAttNameJoin = cAttName;
							break;
						}
						else{
							//do nothing
						}
					}
					
					for(AttribJoin attInformation : rightAttributes){
						AttInfo cAttInfo = attInformation.getAttinfo();
						String cAttName = ""+cAttInfo.getAlias()+"_"+cAttInfo.getAttName();
						if(rAttribute.equals(cAttName)){
							rightDenom = rightDenom * cAttInfo.getOutputCount();
							rAttNameJoin = cAttName;
							break;
						}
						else if(lAttribute.equals(cAttName)){
							leftDenom = leftDenom * cAttInfo.getOutputCount();
							lAttNameJoin = cAttName;
							break;
						}	
						
						else{
							//do nothing
						}
					}
					
					double minValueL = outAttributes.get(lAttNameJoin).getOutputCount();
					double minValueR = outAttributes.get(rAttNameJoin).getOutputCount();
					double minValue = (minValueL < minValueR) ? minValueL : minValueR ;
					
					AttInfo leftAttInfo = outAttributes.get(lAttNameJoin);
					leftAttInfo.setOutputCount(minValue);
					
					AttInfo rightAttInfo = outAttributes.get(rAttNameJoin);			
					rightAttInfo.setOutputCount(minValue);
					
					outAttributes.put(lAttNameJoin, leftAttInfo);
					outAttributes.put(rAttNameJoin, rightAttInfo);					
				}
				
				double minLR = (leftDenom < rightDenom ) ? leftDenom : rightDenom;				
				tOut= minLR * (leftTupleCount/leftDenom)* (rightTupleCount/rightDenom);
				
				int pos = 1;
				ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
				for(Entry<String, AttInfo> outAttribSet : outAttributes.entrySet()){
					AttInfo value = outAttribSet.getValue();
					if(value.getOutputCount() > tOut)
						value.setOutputCount(tOut);					
					
					joinOutAttribts.add(new AttribJoin(value, pos++));
				}
				
				ReturnJoin outputInfo = new ReturnJoin(joinOutAttribts,"costing");
				current.setTupleCount(tOut);
				return outputInfo;				
			}			
		}
		
		else if(current.getType().equals("RA_SELECT_TYPE")){
			IRAType nextNode = current.getNext();			
			
			/**
			 * CASE 1: When the underlying node is the JOIN TYPE
			 * CASE 2: When the underlying node is the SELECT TYPE 
			 */
			
			if(nextNode.getType().equals("RA_JOIN_TYPE") || nextNode.getType().equals("RA_SELECT_TYPE")){
				ReturnJoin prevResult = costing(nextNode);
				ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
				int pos = 1;
				double tOut = 0;
				ArrayList<AttribJoin> prevInAttrbutes = prevResult.getJoinOutAttribts();
				Collections.sort(prevInAttrbutes, new AttribJoinComparator());
				selectExpression.clear();
				ArrayList<Expression> selExpressions = ((RASelectType)current).getSelectPredicate();
				for(Expression exp : selExpressions){
					makeExpression(exp);
				}				
				
				/**
				 * Assuming the case when in the select predicate there is only one
				 * Select predicate which is either equals or
				 * "greater than" or "less than"
				 */				
				for(Expression currentExp: selectExpression){					
					if(currentExp.getType().equals("greater than") || currentExp.getType().equals("less than")){
						double relationTuples = nextNode.getTupleCount();
						Expression leftExpression = currentExp.getLeftSubexpression();
						String attribute = leftExpression.getValue().replace('.', '_');
						
						tOut = (relationTuples/3);
						
						for(AttribJoin attInformation : prevInAttrbutes){
							AttInfo cAttInfo = attInformation.getAttinfo();
							double currentCount = cAttInfo.getOutputCount();
							String attName = ""+ cAttInfo.getAlias()+"_"+cAttInfo.getAttName();							
							if(attName.equals(attribute)){	
								cAttInfo.setOutputCount(currentCount/3);
							}
							else{
								double min = (currentCount > tOut ) ? tOut : currentCount;								
								cAttInfo.setOutputCount(min);
							}
							joinOutAttribts.add(new AttribJoin(cAttInfo, pos++));
						}
						
					}				
					
					else if(currentExp.getType().equals("equals")){
						double relationTuples = nextNode.getTupleCount();
						Expression leftExpression = currentExp.getLeftSubexpression();
						String attribute = leftExpression.getValue().replace('.', '_');

						for(AttribJoin attInformation : prevInAttrbutes){
							AttInfo cAttInfo = attInformation.getAttinfo();
							double currentCount = cAttInfo.getOutputCount();
							
							if(cAttInfo.getAttName().equals(attribute)){								
								cAttInfo.setOutputCount(1);	
								tOut = (relationTuples/currentCount);
							}							
							else{
								double min = (currentCount > tOut ) ? tOut : currentCount;								
								cAttInfo.setOutputCount(min);
							}							
							joinOutAttribts.add(new AttribJoin(cAttInfo, pos++));
						}
					}
					else{
						
					}					
				}
				selectExpression.clear();
				ReturnJoin outputInfo = new ReturnJoin(joinOutAttribts,"costing");
				current.setTupleCount(tOut);
				return outputInfo;
			}			
			
			/**
			 * Case 3: When the underlying node is a TABLE
			 */
			else{
				double tOut = 0;
				ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
				RATableType next = (RATableType)nextNode;
				Map<String, AttInfo> tableMap = next.getAttributesInfo();
				ArrayList<AttInfo> tempInfo = new ArrayList<AttInfo>();
				
				int pos = 1 ;
				for (Entry<String, AttInfo> entry : tableMap.entrySet()) {
					  AttInfo value = entry.getValue();
					  tempInfo.add(value);
					}
				
				
				Collections.sort(tempInfo, new AttInfoComparator());
				selectExpression.clear();
				ArrayList<Expression> selExpressions = ((RASelectType)current).getSelectPredicate();
				for(Expression exp : selExpressions){
					makeExpression(exp);
				}
				
				/**
				 * Assuming the case when in the select predicate there is only one
				 * Select predicate which is either equals or
				 * "greater than" or "less than"
				 */
				for(Expression currentExp: selectExpression){					
					if(currentExp.getType().equals("greater than") || currentExp.getType().equals("less than")){
						double relationTuples = next.getTupleCount();
						Expression leftExpression = currentExp.getLeftSubexpression();
						String attribute = leftExpression.getValue().substring(leftExpression.getValue().indexOf('.')+1);
						int attributeCount = tableMap.get(attribute).getNumDistinctVals();
						tOut = (relationTuples/3);
						for(AttInfo attInformation : tempInfo){
							if(attInformation.getAttName().equals(attribute)){
								attInformation.setOutputCount((attributeCount/3));
							}
							else{
								double min = (attInformation.getNumDistinctVals() > tOut ) ?
												tOut : attInformation.getNumDistinctVals();
								
								attInformation.setOutputCount(min);
							}							
							joinOutAttribts.add(new AttribJoin(attInformation, pos++));
						}
					}				
					
					else if(currentExp.getType().equals("equals")){
						double relationTuples = next.getTupleCount();
						Expression leftExpression = currentExp.getLeftSubexpression();
						String attribute = leftExpression.getValue().substring(leftExpression.getValue().indexOf('.')+1);
						int attributeValueCount = tableMap.get(attribute).getNumDistinctVals();
						tOut = (relationTuples/attributeValueCount);
						for(AttInfo attInformation : tempInfo){
							if(attInformation.getAttName().equals(attribute)){
								attInformation.setOutputCount(1);
							}
							else{
								double min = (attInformation.getNumDistinctVals() > tOut ) ?
												tOut : attInformation.getNumDistinctVals();
								
								attInformation.setOutputCount(min);
							}							
							joinOutAttribts.add(new AttribJoin(attInformation, pos++));
						}
					}
					else{
						
					}					
				}
				selectExpression.clear();
				ReturnJoin outputInfo = new ReturnJoin(joinOutAttribts,"costing");
				current.setTupleCount(tOut);
				return outputInfo;
			}
		}

		/**
		 * The case where the current node
		 * is TABLE type so just returning the table tuple output as the 
		 * number of the tuple count of the base table.
		 */
		else if(current.getType().equals("RA_TABLE_TYPE")){
			RATableType currentNode = (RATableType)current;
			Map<String, AttInfo> tableMap = currentNode.getAttributesInfo();
			ArrayList<AttInfo> tempInfo = new ArrayList<AttInfo>();
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
			double tOut = currentNode.getTupleCount();
			int pos = 1 ;
			for (Entry<String, AttInfo> entry : tableMap.entrySet()) {
				  AttInfo value = entry.getValue();
				  tempInfo.add(value);
				}			
			Collections.sort(tempInfo, new AttInfoComparator());
			
			for(AttInfo attInformation : tempInfo){
				int outCount = attInformation.getNumDistinctVals();
				attInformation.setOutputCount(outCount);
				joinOutAttribts.add(new AttribJoin(attInformation, pos++));
			}
			
			current.setTupleCount(tOut);
			ReturnJoin outputInfo = new ReturnJoin(joinOutAttribts,"costing");
			return outputInfo;
		}
		
		 /**
		 * The base case where the current node
		 * is PROJECT type so just returning the tuple count from the previous node.
		 */
		else{
			RAProjectType currentNode = (RAProjectType)current;
			IRAType nextNode = currentNode.getNext();
			ReturnJoin prevOutput = costing(nextNode);
			double tOut = nextNode.getTupleCount();
			ArrayList<AttribJoin> joinOutAttribts = prevOutput.getJoinOutAttribts();			
			currentNode.setTupleCount(tOut);
			ReturnJoin outputInfo = new ReturnJoin(joinOutAttribts,"costing");
			return outputInfo;
		}		
	}
}
