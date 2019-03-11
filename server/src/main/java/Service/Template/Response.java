package Service.Template;

import Model.Report;

public class Response {
	private Report report;
	
	public Response(String message, boolean status) {
		this.report = new Report(message, status);
	}
	
	public boolean getReportErrorStatus() {
		return this.report.getStatus();
	}
	
	public String getReportErrorMessage() {
		return this.report.getMessage();
	}
	
	public Report getReport() {
		return this.report;
	}
}
