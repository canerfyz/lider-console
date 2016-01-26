package tr.org.liderahenk.liderconsole.core.task;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class QueryImpl {
	private int offset = 0;
	private int maxResults = 20;
	private QueryCriteriaImpl[] criteria;
	
	public QueryImpl(int offset, int maxResults, QueryCriteriaImpl[] criteria) {
		super();
		this.offset = offset;
		this.maxResults = maxResults;
		this.criteria = criteria;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public QueryCriteriaImpl[] getCriteria() {
		return criteria;
	}

	public void setCriteria(QueryCriteriaImpl[] criteria) {
		this.criteria = criteria;
	}	
}
