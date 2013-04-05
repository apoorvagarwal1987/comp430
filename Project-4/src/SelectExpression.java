/**
 * @author apoorvagarwal
 *
 */
public class SelectExpression {
	
	/**
	 * @param type
	 */
	public SelectExpression() {
		this.expType = null;
		leftRAExpression = null;
		rightRAExpression = null;
		topExpression = null;
		botttomExpression =null;
		expression = null;
	}

	private SelectExpression leftRAExpression;
	private SelectExpression rightRAExpression;	
	private String expType;
	private Expression expression;
	private SelectExpression topExpression;
	private SelectExpression botttomExpression;

	/**
	 * @return the leftExpression
	 */
	public SelectExpression getLeftExpression() {
		return leftRAExpression;
	}

	/**
	 * @param leftExpression the leftExpression to set
	 */
	public void setLeftExpression(SelectExpression leftExpression) {
		this.leftRAExpression = leftExpression;
	}

	/**
	 * @return the rightExpression
	 */
	public SelectExpression getRightExpression() {
		return rightRAExpression;
	}

	/**
	 * @param rightExpression the rightExpression to set
	 */
	public void setRightExpression(SelectExpression rightExpression) {
		this.rightRAExpression = rightExpression;
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

	/**
	 * @return the topExpression
	 */
	public SelectExpression getTopExpression() {
		return topExpression;
	}

	/**
	 * @param topExpression the topExpression to set
	 */
	public void setTopExpression(SelectExpression topExpression) {
		this.topExpression = topExpression;
	}

	/**
	 * @return the botttomExpression
	 */
	public SelectExpression getBotttomExpression() {
		return botttomExpression;
	}

	/**
	 * @param botttomExpression the botttomExpression to set
	 */
	public void setBotttomExpression(SelectExpression botttomExpression) {
		this.botttomExpression = botttomExpression;
	}
	
	
}
