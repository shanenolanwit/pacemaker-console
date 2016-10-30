package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

/**
 * XMLSerializer 
 * @author Shane Nolan
 * Used to serialise pacemaker information to an xml datastore
 */
public class XMLSerializer implements Serializer {

	private Stack stack = new Stack();
	private File dataStore = new File("datastore.xml");	

	public XMLSerializer() {
		super();
	}
	
	public XMLSerializer(File dataStore) {
		this.dataStore = dataStore;
	}

	public void push(Object o) {
		stack.push(o);
	}

	public Object pop() {
		return stack.pop();
	}

	public void read() throws Exception {
		ObjectInputStream is = null;
		BufferedReader reader = new BufferedReader(new FileReader(dataStore));
		try {
			XStream xstream = new XStream(new DomDriver());
			is = xstream.createObjectInputStream(reader);
			stack = (Stack) is.readObject();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	public void write() throws Exception {
		ObjectOutputStream os = null;
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataStore));
		try {
			XStream xstream = new XStream(new DomDriver());
			os = xstream.createObjectOutputStream(writer);
			os.writeObject(stack);
		} finally {
			if (os != null) {
				os.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	@Override
	public File getDataStore() {
		return this.dataStore;
	}
}