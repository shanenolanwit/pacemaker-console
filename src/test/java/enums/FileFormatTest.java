package enums;

import static org.junit.Assert.*;

import org.junit.Test;

import utils.BinarySerializer;
import utils.JSONSerializer;
import utils.XMLSerializer;

public class FileFormatTest {

	@Test
	public void testFilterIdentifier() {
		assertEquals(BinarySerializer.class,FileFormat.identify("binary").getSerializer().getClass());
		assertEquals(BinarySerializer.class,FileFormat.identify("BINARY").getSerializer().getClass());
		assertEquals(BinarySerializer.class,FileFormat.identify("biNaRy").getSerializer().getClass());
		
		assertEquals(XMLSerializer.class,FileFormat.identify("xMl").getSerializer().getClass());
		assertEquals(XMLSerializer.class,FileFormat.identify("xml").getSerializer().getClass());
		assertEquals(XMLSerializer.class,FileFormat.identify("XML").getSerializer().getClass());
		
		assertEquals(JSONSerializer.class,FileFormat.identify("JSON").getSerializer().getClass());
		assertEquals(JSONSerializer.class,FileFormat.identify("JsOn").getSerializer().getClass());
		assertEquals(JSONSerializer.class,FileFormat.identify("json").getSerializer().getClass());
		
	}
	
	@Test
	public void testExistance(){
		assertTrue(FileFormat.exists("binary"));
		assertTrue(FileFormat.exists("XML"));
		assertTrue(FileFormat.exists("Json"));
		assertFalse(FileFormat.exists("blah"));
		assertFalse(FileFormat.exists(""));
		assertFalse(FileFormat.exists(null));
	}
	
	@Test(expected = Exception.class)
	public void testBadIdentifierException(){
		FileFormat.identify("j");
	}
	
	@Test(expected = Exception.class)
	public void testNullIdentifierException(){
		FileFormat.identify(null);
	}


}
