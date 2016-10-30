package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

/**
 * BinarySerializer 
 * @author Shane Nolan
 * Used to serialise pacemaker information to a binary datastore
 */
public class BinarySerializer implements Serializer {

	private Stack stack = new Stack();
	private File dataStore = new File("datastore.bin");

	public BinarySerializer() {
		super();
	}
	
	public BinarySerializer(File dataStore) {
		this.dataStore = dataStore;
	}

	public void push(Object o) {
		stack.push(o);
	}

	public Object pop() {
		return stack.pop();
	}

	@SuppressWarnings("unchecked")
	public void read() throws Exception {
		ObjectInputStream is = null;

		try {
			is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataStore)));
			stack = (Stack) is.readObject();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public void write() throws Exception {
		ObjectOutputStream os = null;

		try {
			os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataStore)));
			os.writeObject(stack);
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	@Override
	public File getDataStore() {
		return this.dataStore;
	}
}
