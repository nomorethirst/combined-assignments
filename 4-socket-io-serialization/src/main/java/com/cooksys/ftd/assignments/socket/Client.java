package com.cooksys.ftd.assignments.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.Student;

public class Client {

    /**
     * The client should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" and "host" properties of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.RemoteConfig} object to create a socket that connects to
     * a {@link Server} listening on the given host and port.
     *
     * The client should expect the server to send a {@link com.cooksys.ftd.assignments.socket.model.Student} object
     * over the socket as xml, and should unmarshal that object before printing its details to the console.
     */
    public static void main(String[] args) {
	String configFilePath = "config/config.xml";
	JAXBContext jaxb = Utils.createJAXBContext();
	Config config = Utils.loadConfig(configFilePath, jaxb);
	
	// Use optional args for host/port
	if (args.length == 3) {
	    try {
		int port = Integer.parseInt(args[2]);
                config.getRemote().setHost(args[1]);
                config.getRemote().setPort(port);

	    } catch (NumberFormatException e) {
		System.out.println("Error parsing port arg to int - using config.xml instead.");
		System.out.println();
		System.out.println("usage: java -jar Client [String host int port]");
		System.out.println();
	    }
	}
	
	System.out.println("Welcome to the Cook Systems Student unmarshaller client!");
	System.out.println();

	// Connect to server
	int port = config.getRemote().getPort();
	String host = config.getRemote().getHost();
	Socket socket = null;
	InputStream is = null;
	try {
	    socket = new Socket(host, port);
            is = socket.getInputStream();
            System.out.println(String.format("Connected to server at %s:%d.", host, port));

	} catch (UnknownHostException e) {
	    System.out.println(String.format("Error - Unkown host: %s", host));
	    System.exit(-1);

	} catch (IOException e) {
	    System.out.println(String.format("Error connecting to %s:%d.", host, port));
	    System.exit(-1);

	} 
	
	// Unmarshal Student object and print to stdout
	Unmarshaller unmarshaller = null;
	Student student = null;
	try {
	    unmarshaller = jaxb.createUnmarshaller();
	    student = (Student) unmarshaller.unmarshal(is);
            System.out.println(String.format("Student object successfully received:", host, port));
	    System.out.println(student);

	} catch (JAXBException e) {
	    System.out.println("Error unmarshalling student object.");

	} finally {
	    try {
                if (is != null) is.close();
                if (is != null) socket.close();
	    } catch (IOException e) {
		System.out.println("Error closing system resources.");
	    }
	}
    }
}
