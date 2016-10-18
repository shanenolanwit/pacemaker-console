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

public class XMLSerializer implements Serializer {

	private Stack stack = new Stack();
	private File file;

	public XMLSerializer(File file) {
		this.file = file;
	}

	public void push(Object o) {
		stack.push(o);
	}

	public Object pop() {
		return stack.pop();
	}

	public void read() throws Exception {
		ObjectInputStream is = null;
		BufferedReader reader = new BufferedReader(new FileReader(file));
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
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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
}