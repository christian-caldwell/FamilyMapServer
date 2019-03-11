package Service.Person;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import Model.Person;
import Model.User;
import Server.MainServer;
import Server.ServerOutput;
import Service.Template.Handler;

public class PersonHandler extends Handler implements HttpHandler  { 
	public PersonHandler (MainServer server, ServerOutput output) {
		super(server, output);
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {
		
		printInfo(httpExchange, "Person");

		String[] params= seperateExchange(httpExchange);
		String token = httpExchange.getRequestHeaders().getFirst("Authorization");
		
		if(token == null || token.isEmpty()) {
			output.createAndSend("Missing or Bad access token",httpExchange);
			System.out.println("    Auth Token: " + "<<No Token Given>>");
		}
		else {
			System.out.println("    Auth Token: " + token);
			PersonRequest request = new PersonRequest(token, params);
			PersonProvider provider = new PersonProvider();
			PersonResponse response = provider.execute(request);
			if (response.getReportErrorMessage() != null) {
				this.output.createAndSend(response.getReportErrorMessage(), httpExchange);
			}
			else if (response.getPerson() != null) {
				this.output.sendOutData(response.getPerson(), httpExchange);
			}
			else if (response.getJson() != null){
				this.output.sendOutData(response.getJson(), httpExchange);
			}
			
		}

		
	}
}
