package com.cooksys.ftd.assignments.socket;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

/**
 * Shared static methods to be used by both the {@link Client} and {@link Server} classes.
 */
public class Utils {
    /**
     * @return a {@link JAXBContext} initialized with the classes in the
     * com.cooksys.socket.assignment.model package
     */
    public static JAXBContext createJAXBContext() {
	JAXBContext jaxb = null;

	try {
	    jaxb = JAXBContext.newInstance(Config.class, LocalConfig.class, 
	    	RemoteConfig.class, Student.class);
	} catch (JAXBException e) {
	    System.out.println("Error creating JAXBContext");
	    e.printStackTrace();
	}

	return jaxb;
    }

    /**
     * Reads a {@link Config} object from the given file path.
     *
     * @param configFilePath the file path to the config.xml file
     * @param jaxb the JAXBContext to use
     * @return a {@link Config} object that was read from the config.xml file
     */
    public static Config loadConfig(String configFilePath, JAXBContext jaxb) {
	Unmarshaller unmarshaller = null;
	Config config = null;

	try {
	    unmarshaller = jaxb.createUnmarshaller();
	    config = (Config) unmarshaller.unmarshal(new File(configFilePath));
	} catch (JAXBException e) {
	    System.out.println(String.format("Error importing config from %s", configFilePath));
	    e.printStackTrace();
	}

        return config;
    }

    public static void main(String[] args) {
	// Generate initial student/config.xml
	JAXBContext ctx;
	Marshaller marshaller = null;
	try {
	    ctx = createJAXBContext();
            marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	} catch (JAXBException e) {
	    e.printStackTrace();
	}

	Student s = new Student();
	s.setFirstName("John");
	s.setLastName("Doe");
	s.setFavoriteLanguage("PDP 11 Machine Language");
	s.setFavoriteParadigm("What's a paradigm?");
	s.setFavoriteIDE("Text Editor, DotMatrix Printer, PDP 11 button panel");
	
	LocalConfig local = new LocalConfig();
	local.setPort(8099);
	RemoteConfig remote = new RemoteConfig();
	remote.setHost("127.0.0.1");
	remote.setPort(8099);
	Config config = new Config();
	config.setLocal(local);
	config.setRemote(remote);
	config.setStudentFilePath("config/student.xml");
	
	try {
            FileOutputStream fos = null;
	    fos = new FileOutputStream("config/student.xml");
            marshaller.marshal(s,  fos);
	    fos.close();
	    fos = new FileOutputStream("config/config.xml");
            marshaller.marshal(config,  fos);
	    fos.close();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
