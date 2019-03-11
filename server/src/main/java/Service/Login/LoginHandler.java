package Service.Login;

import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;

import Model.AuthToken;
import Model.User;
import Server.MainServer;
import Server.ServerOutput;
import Server.ServerInput;
import Service.Template.Handler;

import Model.User;

public class LoginHandler extends Handler implements HttpHandler  {
	private ServerInput input;
	private User currUser;
	private Gson gson = new Gson();
	
	public LoginHandler (MainServer server, ServerOutput output, ServerInput input) {
		super(server, output);
		this.input = input;
		this.currUser = null;
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {
		
		printInfo(httpExchange, "User/Login");
		seperateExchange(httpExchange);
		
		try
		{
			InputStream body=httpExchange.getRequestBody();
			String bodyParts= input.streamToString(body);
			System.out.println("    Response Body: " + bodyParts);
			
			JsonObject json = gson.fromJson(bodyParts, JsonObject.class);

			
			if(json == null || !(json.has("userName"))) {
				output.createAndSend("Username Not Provided", httpExchange);
			}
			else if(json == null || !(json.has("password"))) {
				output.createAndSend("Password Not Provided", httpExchange);
			}
			else {
				if(json.get("password").getAsString().isEmpty() | json.get("userName").getAsString().isEmpty() ) {
					output.createAndSend("UserName and Password Cannot Be Empty", httpExchange);
				}
				else {
					this.currUser = new User();
					this.currUser.setUserName(json.get("userName").getAsString());
					this.currUser.setPassword(json.get("password").getAsString());

					LoginRequest request = new LoginRequest(this.currUser);
					LoginProvider provider = new LoginProvider();
					LoginResponse response = provider.execute(request);
					if (response.getReportErrorMessage() != null) {
						this.output.createAndSend(response.getReportErrorMessage(), httpExchange);
					}
					else if (response.getToken() != null) {
						this.output.sendOutData(response.getToken(), httpExchange);
					}
				}

			}
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("    Error with stream " + e.getMessage());
		}
	}
}
