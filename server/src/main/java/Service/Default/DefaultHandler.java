package Service.Default;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import com.sun.net.httpserver.*;

import Server.MainServer;
import Server.ServerOutput;
import Service.Template.Handler;

public class DefaultHandler extends Handler implements HttpHandler  {
	
	
	public DefaultHandler (MainServer server, ServerOutput output) {
		super(server, output);
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {
		try {
			printInfo(httpExchange, "Default");
			Headers head = httpExchange.getResponseHeaders();
			//head.set("Content-Type", "text/html");


			URI command = httpExchange.getRequestURI();
			String theCommand = command.toString();

			System.out.println("    Command received: " + theCommand);
			String[] params = theCommand.split("/", 2);

			String path = null;
			if (params.length <= 1 || params[1].equals("") || params[1].equals("index.html")) {
				path = "index.html";
				String filePathStr = "HTML/index.html";
				head.set("Content-Type", "text/html");
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				Path filePath = FileSystems.getDefault().getPath(filePathStr);
				Files.copy(filePath, httpExchange.getResponseBody());
				httpExchange.getResponseBody().close();
			} else {
				path = params[1];
				if (theCommand.split("/")[1].equals("css")) {

					String filePathStr = "HTML/css/main.css";
					head.set("Content-Type", "text/css");
					httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					Path filePath = FileSystems.getDefault().getPath(filePathStr);
					Files.copy(filePath, httpExchange.getResponseBody());
					httpExchange.getResponseBody().close();
				} else if (theCommand.split("/")[1].equals("favicon.ico")) {
					head.set("Content-Type", "image/png");
					httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					String filePathStr = "HTML/favicon.ico";
					Path filePath = FileSystems.getDefault().getPath(filePathStr);
					Files.copy(filePath, httpExchange.getResponseBody());
					httpExchange.getResponseBody().close();
				} else if (theCommand.split("/")[1].equals("favicon.jpg")) {
					head.set("Content-Type", "image/png");
					httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					String filePathStr = "HTML/favicon.jpg";
					Path filePath = FileSystems.getDefault().getPath(filePathStr);
					Files.copy(filePath, httpExchange.getResponseBody());
					httpExchange.getResponseBody().close();
				} else {
					head.set("Content-Type", "text/html");
					httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					path = "404.html";
					String filePathStr = "HTML/404.html";
					Path filePath = FileSystems.getDefault().getPath(filePathStr);
					Files.copy(filePath, httpExchange.getResponseBody());
					httpExchange.getResponseBody().close();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			httpExchange.close();
		}
	}
}
