package Service.Register;

import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import Model.User;
import Server.MainServer;
import Server.ServerOutput;
import Server.ServerInput;
import Service.Template.Handler;

public class RegisterHandler extends Handler implements HttpHandler  {
	private int generations;
	private ServerInput input;
	private User currUser;
	private Gson gson = new Gson();
	
	public RegisterHandler (MainServer server, ServerOutput output,  ServerInput input, int generations) {
		super(server, output);
		this.generations = generations;
		this.input = input;
		this.currUser = null;
		
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {
		
		printInfo(httpExchange, "User/Register");
		seperateExchange(httpExchange);
		
		try
		{
			InputStream body=httpExchange.getRequestBody();
			String bodyParts= input.streamToString(body);
			System.out.println("    Request Body: " + bodyParts);
			JsonObject json = gson.fromJson(bodyParts, JsonObject.class);
			
			if (json == null) {
				output.createAndSend("Json Object Creation Failed, Check Input", httpExchange);
			}
			else if (!json.has("userName")) {
				output.createAndSend("Missing user name in post body", httpExchange);
			}
			else if(!json.has("password")) {
				output.createAndSend("Missing password in post body", httpExchange);
			}
			else if(!json.has("email")) {
				output.createAndSend("Missing email in post body", httpExchange);
			}
			else if(!json.has("firstName")) {
				output.createAndSend("Missing first name in post body", httpExchange);
			}
			else if(!json.has("lastName")) {
				output.createAndSend("Missing last name in post body", httpExchange);
			}
			else if(!json.has("gender")) {
				output.createAndSend("Missing gender in post body", httpExchange);
			}
			else {
				System.out.println("1");
				String gender = json.get("gender").getAsString().toLowerCase();
				if(!gender.equals("m") && !gender.equals("f")) {
					output.createAndSend("Gender parameter incorrectly formated", httpExchange);
				}
				else if(json.get("password").getAsString().isEmpty()  || json.get("userName").getAsString().isEmpty()) {
					output.createAndSend("UserName and Password Cannot Be Empty", httpExchange);
				}
				else if(json.get("firstName").getAsString().isEmpty() || json.get("lastName").getAsString().isEmpty() ) {
					output.createAndSend("First and Last Name Cannot Be Empty", httpExchange);
				}
				else if(json.get("email").getAsString().isEmpty() ) {
					output.createAndSend("Email Cannot Be Empty", httpExchange);
				}
				else {

					this.currUser = new User();
					this.currUser.setUserName(json.get("userName").getAsString());
					this.currUser.setGender(gender);
					this.currUser.setPassword(json.get("password").getAsString());
					this.currUser.setEmail(json.get("email").getAsString());
					this.currUser.setFirstName(json.get("firstName").getAsString());
					this.currUser.setLastName(json.get("lastName").getAsString());
					RegisterRequest request = new RegisterRequest(this.currUser, this.generations);
					RegisterProvider provider = new RegisterProvider();
					RegisterResponse response = provider.execute(request);
					if (response.getReportErrorMessage() != null) {
						this.output.createAndSend(response.getReportErrorMessage(), httpExchange);
					}
					else if (response.getToken() != null) {
						this.output.sendOutData(response.getToken(), httpExchange);
					}
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("    Error with stream " + e.getMessage());
		}
			
	}
}
