package tr.org.liderahenk.liderconsole.core.task;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class QueryCriteriaImpl {
	
	private String field;
	private CriteriaOperator operator;
	private Object[] values = new Object[] {};
	
	public QueryCriteriaImpl()
	{
		
	}
	
	public QueryCriteriaImpl(String field, CriteriaOperator operator,
			Object... values) {
		this.field = field;
		this.operator = operator;
		this.values = values;
	}
	
	public String getField() {
		return field;
	}

	public void setField(final String field) {
		this.field = field;
	}
	
	public CriteriaOperator getOperator() {
		return operator;
	}

	public void setOperator(final CriteriaOperator operator) {
		this.operator = operator;
	}

	
	public Object[] getValues() {
		return values;
	}
	
	public void setValues(Object[] values) {
		this.values = values;
	}	
}
