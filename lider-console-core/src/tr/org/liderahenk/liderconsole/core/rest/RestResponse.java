package tr.org.liderahenk.liderconsole.core.rest;

public class RestResponse {
	private int status;
	private ServerResult result;
	private String stringResult;
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setResult(ServerResult result) {
		this.result = result;
	}
	
	public int getStatus() {
		return status;
	}
	
	public ServerResult getResult() {
		return result;
	}

	public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
	}	
}
