package Service.Fill;

import java.util.Map;

import com.sun.net.httpserver.*;

import Server.MainServer;
import Server.ServerOutput;
import Service.Clear.ClearProvider;
import Service.Clear.ClearRequest;
import Service.Clear.ClearResponse;
import Service.Template.Handler;

import Server.ServerInput;

public class FillHandler extends Handler implements HttpHandler  {
	
	private ServerInput input;
	private int generations;

	public FillHandler (MainServer server, ServerOutput output, ServerInput input ,int generations) {
		super(server, output);
		this.input = input;
		this.generations = generations;
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {
		
		printInfo(httpExchange, "Fill");
		
		String[] params = seperateExchange(httpExchange); 
		
		int levels = this.generations;
		String username;
		
		if (params.length == 3 || params.length == 4) {
			if (params.length == 4 ) {
				levels = Integer.parseInt(params[3]);
			}
			if(levels >= 0) {
				username = params[2];
				FillRequest request = new FillRequest(username, levels);
				FillProvider provider = new FillProvider();
				FillResponse response = provider.execute(request);
				this.output.createAndSend(response.getReportErrorMessage(), httpExchange);
			}
			else {
				output.createAndSend("Failed. Generations Must Be Greater Or Equal to Zero.", httpExchange);

			}
		}
		else {
			output.createAndSend("Failed. Incorrect Number of Parameters.", httpExchange);
		}
	}
}
