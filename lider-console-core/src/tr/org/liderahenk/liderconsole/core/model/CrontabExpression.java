package tr.org.liderahenk.liderconsole.core.model;

public class CrontabExpression {

	private String crontabStr;
	private boolean active;

	/**
	 * 
	 * @return cron expression. Example: 0 0 12 1/1 * ? *
	 */
	public String getCrontabStr() {
		return crontabStr;
	}

	public void setCrontabStr(final String crontabStr) {
		this.crontabStr = crontabStr;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

}
