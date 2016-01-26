package tr.org.liderahenk.liderconsole.core.schedule;

public class ScheduleRequestAdapter {

	private CrontabExpression ctExpr;
	
	public ScheduleRequestAdapter(){ 
		// No initial value assignment to the fields.
	}
	
	public ScheduleRequestAdapter(final CrontabExpression expression) {
		ctExpr = expression;
	}
	
	public ScheduleRequest plug() {
		final ScheduleRequest req = new ScheduleRequest();
		req.setActive(true);
		req.setCronString( ctExpr.getCrontabStr() );
		req.setEndDate(null);
		req.setStartDate(null);
		req.setOperation(ScheduleOperation.ADD);
		req.setScheduleId(null);
		
		return req;
	}
}
