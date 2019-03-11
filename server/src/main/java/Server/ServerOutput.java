package Server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;

public class ServerOutput {
	private Gson gson = new Gson();
	
	public void sendOutData(Object obj, HttpExchange exchange)
	{
		try
		{
			if (obj != null)
			{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

				OutputStreamWriter sendBack = new OutputStreamWriter(exchange.getResponseBody(), Charset.forName("UTF-8"));
				String json = gson.toJson(obj);
				sendBack.write(json);
				sendBack.close();
			}
			else
			{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("\nError sending out the data " + e.getMessage());
		}
	}
	
	private JsonObject makeMessage(String message)
	{
		JsonObject obj = new JsonObject();
		obj.addProperty("message",message);
		return obj;
	}
	
	public void createAndSend(String message, HttpExchange exchange) {
		sendOutData(makeMessage(message), exchange); 
	}
}
