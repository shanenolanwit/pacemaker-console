package enums;

import utils.BinarySerializer;
import utils.JSONSerializer;
import utils.Serializer;
import utils.XMLSerializer;
import utils.YAMLSerializer;


public enum FileFormat {	
	
	BINARY(new BinarySerializer()),
	JSON(new JSONSerializer()),
	XML(new XMLSerializer()),
	YAML(new YAMLSerializer());
	
	private final Serializer serializer;
	
	private FileFormat(Serializer serializer){
		this.serializer = serializer;
	}	
	
	public Serializer getSerializer() {
		return serializer;
	}

	public static FileFormat identify(String identifier){
		return FileFormat.valueOf(identifier.toUpperCase());
	}

	public static boolean exists(String sortBy) {
		boolean exists = false;
		try{
			FileFormat.valueOf(sortBy.toUpperCase());
			exists = true;
		} catch(IllegalArgumentException | NullPointerException e){
			
		}
		return exists;
	}
	
}
