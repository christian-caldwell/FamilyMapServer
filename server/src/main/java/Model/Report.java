package Model;

public class Report
{
    private boolean status;
    private String message;

    public Report(String message, boolean status)
    {
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
