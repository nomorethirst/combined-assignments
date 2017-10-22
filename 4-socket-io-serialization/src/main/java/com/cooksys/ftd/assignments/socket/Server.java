package com.cooksys.ftd.assignments.socket;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.Student;

public class Server extends Utils {

    /**
     * Reads a {@link Student} object from the given file path
     *
     * @param studentFilePath the file path from which to read the student config file
     * @param jaxb the JAXB context to use during unmarshalling
     * @return a {@link Student} object unmarshalled from the given file path
     */
    public static Student loadStudent(String studentFilePath, JAXBContext jaxb) {
	Unmarshaller unmarshaller = null;
	Student student = null;

	try {
	    unmarshaller = jaxb.createUnmarshaller();
	    student = (Student) unmarshaller.unmarshal(new File(studentFilePath));
	} catch (JAXBException e) {
	    System.out.println(String.format("Error importing student from %s", studentFilePath));
	    e.printStackTrace();
	}

        return student;

    }
    
    /**
     * Creates a ServerSocket 
     * @return ServerSocket 
     */
    public static ServerSocket startTcpServer(int port) {
	ServerSocket server = null;
	try {
	    server = new ServerSocket(port);
            System.out.println(String.format("Server listening on port %d...", server.getLocalPort()));
	} catch (IOException e) {
	    System.out.println("Server could not start.");
	    e.printStackTrace();
	}
	return server;
    }
    
    /**
     * Accepts a TCP connection from client and returns the socket.
     * @param ServerSocket a listening ServerSocket
     * @return Socket a successfully accepted TCP socket connection with client.
     */
    public static Socket acceptTcpClient(ServerSocket server) {
	Socket client = null;
	try {
            client = server.accept();
            System.out.println(String.format("Accepted connection from %s.", 
        	    	client.getInetAddress().toString()));
	} catch (IOException e) {
	    System.out.println("Error accepting connection from client.");
	    e.printStackTrace();
	}
	return client;
    }

    /**
     * The server should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" property of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.LocalConfig} object to create a server socket that
     * listens for connections on the configured port.
     *
     * Upon receiving a connection, the server should unmarshal a {@link Student} object from a file location
     * specified by the config's "studentFilePath" property. It should then re-marshal the object to xml over the
     * socket's output stream, sending the object to the client.
     *
     * Following this transaction, the server may shut down or listen for more connections.
     */
    public static void main(String[] args) {
	String configFilePath = "config/config.xml";
	JAXBContext jaxb = createJAXBContext();
	Config config = loadConfig(configFilePath, jaxb);
	
	System.out.println("Welcome to the Cook Systems Student marshalling server!");
	System.out.println();
	
	ServerSocket server = startTcpServer(config.getLocal().getPort());

	Socket client = acceptTcpClient(server);
	
	Student student = loadStudent(config.getStudentFilePath(), jaxb);
	Marshaller marshaller = null;
	OutputStream os = null;
	try {
	    marshaller = jaxb.createMarshaller();
	    os = client.getOutputStream();
	    marshaller.marshal(student, os);
	    System.out.println("Student object successfully marshalled over socket - closing connection.");
	} catch (JAXBException e) {
	    System.out.println("Error creating marshaller.");
	    e.printStackTrace();
	} catch (IOException e) {
	    System.out.println("Error marshalling student over socket.");
	    e.printStackTrace();
	} 

        finally {
            try {
                os.close();
                client.close();
                server.close();
            } catch (IOException e) {
                System.out.println("Error closing system resources.");
                e.printStackTrace();
            }
        }
    }
}
