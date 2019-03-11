package Service.Event;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import Model.Event;
import Model.User;
import Server.MainServer;
import Server.ServerOutput;
import Service.Person.PersonResponse;
import Service.Template.Handler;

public class EventHandler extends Handler implements HttpHandler  {
	public EventHandler (MainServer server, ServerOutput output) {
		super(server, output);
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {
		
		printInfo(httpExchange, "Event");
		String[] params= seperateExchange(httpExchange);
		String token = httpExchange.getRequestHeaders().getFirst("Authorization");

		
		
		if(params.length < 2) {
			output.createAndSend("Please specify more info (event OR person OR fill OR users)", httpExchange);
		}
		else if(token == null) {
			output.createAndSend("Missing or Bad access token",httpExchange);
			System.out.println("    Auth Token: " + token);
		}
		else{
			System.out.println("    Auth Token: " + token);
			EventRequest request = new EventRequest(token, params);
			EventProvider provider = new EventProvider();
			EventResponse response = provider.execute(request);
			if (response.getReportErrorMessage() != null) {
				this.output.createAndSend(response.getReportErrorMessage(), httpExchange);
			}
			else if (response.getEvent() != null) {
				this.output.sendOutData(response.getEvent(), httpExchange);
			}
			else if (response.getJson() != null){
				this.output.sendOutData(response.getJson(), httpExchange);
			}
		}
			

		
	}
}
