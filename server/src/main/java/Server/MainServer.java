package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;

import com.sun.net.httpserver.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Data.DataBase;
import Model.Report;

import Service.Clear.ClearHandler;
import Service.Fill.FillHandler;
import Service.Person.PersonHandler;
import Service.Event.EventHandler;
import Service.Login.LoginHandler;
import Service.Register.RegisterHandler;
import Service.Default.DefaultHandler;
import Service.Load.LoadHandler;

public class MainServer 
{
	private static final int MAX_WAITING_CONNECTION = 10;
	private static int DEFAULT_GENERATIONS = 4;
	
	private HttpServer server;
	private static int SERVER_PORT_NUMBER;;
	private Gson gson = new Gson();
	private DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

	private ServerInput input;
	private ServerOutput output;
	
	private ClearHandler clearHandler;
	private FillHandler fillHandler;
	private PersonHandler personHandler;
	private EventHandler eventHandler;
	private LoginHandler loginHandler;
	private RegisterHandler registerHandler;
	private DefaultHandler defaultHandler;
	private LoadHandler loadHandler;
	
	public static void main(String[] args)		
	{
		System.out.println(System.getProperty("user.dir"));
		File htmlF = new File("HTML");
		File dataF = new File("data");
		
		if(!htmlF.exists()) 
		{
			System.out.println("The HTML folder is missing. The server cannot run without it. Shutting down!");
		}
		else if(!dataF.exists()) 
		{
			System.out.println("The data folder is missing. The server cannot run without it. Shutting down!");
			return;
		}
		else if(args.length == 0)
		{
			SERVER_PORT_NUMBER = 8080;		
			System.out.println("Server started on port: " + 8080);
			new MainServer().run();	
		}
		else if(args.length == 1) {
			SERVER_PORT_NUMBER = Integer.valueOf(args[0]);		
			System.out.println("Server started on port: " + args[0]);
			new MainServer().run();	
		}
		else {
			System.out.println("More arguments needed. Please specify the port number and optionally"
					+ " the default number of max generations that are allowed on the /fill/ API."
					+ " eg: SERVER.JAR 8080 5");
		}
	}
	
	public MainServer() {
		this.input = new ServerInput();
		this.output = new ServerOutput();
		
		this.clearHandler = new ClearHandler(this, output);
		this.fillHandler = new FillHandler(this, output, input, this.DEFAULT_GENERATIONS);
		this.personHandler = new PersonHandler(this, output);
		this.eventHandler = new EventHandler(this, output);
		this.loginHandler = new LoginHandler(this, output, input);
		this.registerHandler = new RegisterHandler(this, output, input, this.DEFAULT_GENERATIONS);
		this.defaultHandler = new DefaultHandler(this, output);
		this.loadHandler = new LoadHandler(this, output, input);
	}
	
	private void run()
	{
		try
		{
			server = HttpServer.create(
					new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTION);
		}
		catch (IOException e)
		{
			System.out.println("Could not create HTTP server: " + e.getMessage());
			
			if(e.getMessage().contains("Address already in use"))
			{
				System.out.println("You have another server already running on this port. Close it"
						+ " and try again");
			}
			else
			{
				e.printStackTrace();
			}
			return;
		}
		
		server.setExecutor(null);

		server.createContext("/load", loadHandler);
		server.createContext("/clear", clearHandler);
		server.createContext("/fill", fillHandler);
		server.createContext("/person", personHandler);
		server.createContext("/event", eventHandler);
		server.createContext("/user/login", loginHandler);
		server.createContext("/user/register", registerHandler);
		server.createContext("/", defaultHandler);
		
		server.start();
	}
}
