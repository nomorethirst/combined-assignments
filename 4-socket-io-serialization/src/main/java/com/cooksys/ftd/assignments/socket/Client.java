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

	int port = config.getRemote().getPort();
	String ipAddr = config.getRemote().getHost();
	Socket socket = null;
	InputStream is = null;
	try {
	    socket = new Socket(ipAddr, port);
            is = socket.getInputStream();
	} catch (UnknownHostException e) {
	    System.out.println(String.format("Unkown host: %s", ipAddr));
	    e.printStackTrace();
	} catch (IOException e) {
	    System.out.println(String.format("Error connecting to %s:%d", ipAddr, port));
	    e.printStackTrace();
	}
	
	Unmarshaller unmarshaller = null;
	Student student = null;
	try {
	    unmarshaller = jaxb.createUnmarshaller();
	    student = (Student) unmarshaller.unmarshal(is);
	    System.out.println(student);
	} catch (JAXBException e) {
	    System.out.println("Error unmarshalling student object.");
	    e.printStackTrace();
	}
	
	finally {
	    try {
		is.close();
		socket.close();
	    } catch (IOException e) {
		System.out.println("Error closing resources.");
		e.printStackTrace();
	    }
	}
	

    }
}
