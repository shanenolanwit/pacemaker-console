package utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

public class JSONSerializer implements Serializer
{

  private Stack stack = new Stack();
  private File file;

  public JSONSerializer(File file)
  {
    this.file = file;
  }

  public void push(Object o)
  {
    stack.push(o);
  }

  public Object pop()
  {
    return stack.pop(); 
  }

  public void read() throws Exception
  {
    ObjectInputStream is = null;
//	BufferedReader reader = new BufferedReader(new FileReader(file));
    JsonReader reader = new JsonReader(new FileReader(file));
    try
    {
//      XStream xstream = new XStream(new JettisonMappedXmlDriver());
//      is = xstream.createObjectInputStream(reader);
//      stack = (Stack) is.readObject();
    	Gson gson = new Gson();
    	
    	stack = (Stack) gson.fromJson(reader, Stack.class);
    }
    finally
    {
    	if(reader != null){
    		reader.close();
    	}
      if (is != null)
      {
        is.close();
      }
      
    }
  }

  public void write() throws Exception
  {
    ObjectOutputStream os = null;
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    try
    {
//      XStream xstream = new XStream(new JettisonMappedXmlDriver());      
//      os = xstream.createObjectOutputStream(writer);
//      os.writeObject(stack);
    	String json = new Gson().toJson(stack);
    	writer.write(json);
    }
    finally
    {
      if (os != null)
      {
        os.close();
      }
      if (writer != null){
    	  writer.close();
      }
    }
  }
}
