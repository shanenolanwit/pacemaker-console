package enums;

import utils.BinarySerializer;
import utils.FileLogger;
import utils.JSONSerializer;
import utils.Serializer;
import utils.XMLSerializer;
import utils.YAMLSerializer;

/**
 * FileFormat links enum values to serializers
 * @author Shane Nolan
 *  Available Filters
 *  <ul>
 *  <li>{@link #BINARY}</li>
 *  <li>{@link #JSON}</li>
 *  <li>{@link #XML}</li>
 *  <li>{@link #YAML}</li>
 *  </ul>
 *
 */
public enum FileFormat {	
	
	/**
	 * @see utils.BinarySerializer
	 */
	BINARY(new BinarySerializer()),
	/**
	 * @see utils.JSONSerializer
	 */
	JSON(new JSONSerializer()),
	/**
	 * @see utils.XMLSerializer
	 */
	XML(new XMLSerializer()),
	/**
	 * @see utils.YAMLSerializer
	 */
	YAML(new YAMLSerializer());
	
	private final Serializer serializer;
	
	private FileFormat(Serializer serializer){
		this.serializer = serializer;
	}	
	
	public Serializer getSerializer() {
		return serializer;
	}

	/**
	 * Identifies which {@link enums.FileFormat} to use based on a given input
	 * @param identifier  A string representing a {@link enums.FileFormat}
	 * <p>The identifier is not case sensitive
	 * Valid identifiers are (XML|JSON|BINARY|YAML)
	 * @return  {@link enums.FileFormat} associated with the given identifier
	 */
	public static FileFormat identify(String identifier){
		return FileFormat.valueOf(identifier.toUpperCase());
	}

	/**
	 * Checks if a given identifier represents a valid {@link enums.FileFormat}
	 * @param identifier  A string representing the type of {@link enums.FileFormat}
	 * @return  boolean representing whether the identifier refers to a valid {@link enums.FileFormat}
	 */
	public static boolean exists(String identifier) {
		boolean exists = false;
		try{
			FileFormat.valueOf(identifier.toUpperCase());
			exists = true;
		} catch(IllegalArgumentException | NullPointerException e){
			FileLogger.getLogger().log(e.getMessage());
		}
		return exists;
	}
	
}
