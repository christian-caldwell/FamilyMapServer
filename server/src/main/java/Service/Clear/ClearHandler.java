package Service.Clear;

import com.sun.net.httpserver.*;

import Server.MainServer;
import Server.ServerOutput;
import Service.Template.Handler;

public class ClearHandler extends Handler implements HttpHandler  {
	
	public ClearHandler (MainServer server, ServerOutput output) {
		super(server, output);
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {

		printInfo(httpExchange, "Clear");
		
		ClearRequest request = new ClearRequest();
		ClearProvider provider = new ClearProvider();
		ClearResponse response = provider.execute(request);
		this.output.createAndSend(response.getReportErrorMessage(), httpExchange);


	}
}

