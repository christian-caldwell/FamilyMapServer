package Service.Template;

import java.net.URI;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.sun.net.httpserver.*;

import Server.MainServer;
import Server.ServerOutput;

public class Handler {
	public MainServer server;
	public ServerOutput output;
	
	public Handler(MainServer server, ServerOutput output) {
		this.server = server;
		this.output = output;
	}
	
	public void printInfo(HttpExchange httpExchange, String name) {
		DateFormat format = new SimpleDateFormat("hh:mm:ss a");
		Calendar cal = Calendar.getInstance();
		System.out.println(name +" API was just called at " + format.format(cal.getTime()));
		URI command=httpExchange.getRequestURI();
		String theCommand=command.toString();
		System.out.println("    Received URI: " + theCommand);
	}
	
	public String[] seperateExchange(HttpExchange httpExchange) {
		URI requestURI = httpExchange.getRequestURI();
		String stringURI = requestURI.toString();
		return stringURI.split("/");
	}
}
