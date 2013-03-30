/**
 * @author apoorvagarwal
 *
 */
public class RAExpression {
	
	/**
	 * @param type
	 */
	public RAExpression(String type) {
		this.expType = type;
	}

	private RAExpression leftExpression;
	private RAExpression rightExpression;	
	private String expType;
	private Expression expression;

	/**
	 * @return the leftExpression
	 */
	public RAExpression getLeftExpression() {
		return leftExpression;
	}

	/**
	 * @param leftExpression the leftExpression to set
	 */
	public void setLeftExpression(RAExpression leftExpression) {
		this.leftExpression = leftExpression;
	}

	/**
	 * @return the rightExpression
	 */
	public RAExpression getRightExpression() {
		return rightExpression;
	}

	/**
	 * @param rightExpression the rightExpression to set
	 */
	public void setRightExpression(RAExpression rightExpression) {
		this.rightExpression = rightExpression;
	}

	/**
	 * @return the expType
	 */
	public String getExpType() {
		return expType;
	}

	/**
	 * @param expType the expType to set
	 */
	public void setExpType(String expType) {
		this.expType = expType;
	}

	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	
}
