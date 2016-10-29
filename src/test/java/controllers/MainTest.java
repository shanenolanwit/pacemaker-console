package controllers;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class MainTest {

	Main main;
	ByteArrayOutputStream outContent;
	PrintStream stdout = System.out;

	@Before
	public void setUp() throws Exception {		
		main = new Main();
		clear();
	}

	@After
	public void tearDown() throws Exception {
		  System.setOut(stdout);
	}
	
	private void clear(){
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	/**
	 * Given the System output has been redirected to a ByteArrayOutputStream
	 * When I invoke the system print method
	 * The message is sent to the byte stream instead of the console
	 */
	@Test
	public void validatingSystemOutRedirection() {
		System.out.print("hello");
	    assertEquals("hello", outContent.toString());
	}
	
	/**
	 * Given I have redirected the system out
	 * When I create a user
	 * Then the byte stream contains a user table
	 */
	@Test
	public void createUser(){	
		int tableCount = StringUtils.countMatches(outContent.toString(), "FIRSTNAME");
		assertThat(tableCount, is(0));
		
		main.createUser("tony", "stark", "ironman@marvel.com", "secret");
		
		assertThat(outContent.toString(), containsString("Tony"));
		assertThat(outContent.toString(), containsString("Stark"));
		assertThat(outContent.toString(), containsString("ironman@marvel.com"));
		assertThat(outContent.toString(), containsString("secret"));
		
		assertThat(outContent.toString(), not(containsString("Clark")));
		assertThat(outContent.toString(), not(containsString("Kent")));
		assertThat(outContent.toString(), not(containsString("superman@dc.com")));
		
		tableCount = StringUtils.countMatches(outContent.toString(), "FIRSTNAME");
		assertThat(tableCount, is(1));
		
		main.createUser("clark", "kent", "superman@dc.com", "secret");
		
		assertThat(outContent.toString(), containsString("Tony"));
		assertThat(outContent.toString(), containsString("Stark"));
		assertThat(outContent.toString(), containsString("ironman@marvel.com"));
		assertThat(outContent.toString(), containsString("secret"));
		
		assertThat(outContent.toString(), containsString("Clark"));
		assertThat(outContent.toString(), containsString("Kent"));
		assertThat(outContent.toString(), containsString("superman@dc.com"));
		assertThat(outContent.toString(), containsString("secret"));
		
		assertThat(outContent.toString(), not(containsString("Peter")));
		assertThat(outContent.toString(), not(containsString("Parker")));
		assertThat(outContent.toString(), not(containsString("spiderman@marvel.com")));
		assertThat(outContent.toString(), not(containsString("spidey")));
		
		tableCount = StringUtils.countMatches(outContent.toString(), "FIRSTNAME");
		assertThat(tableCount, is(2));
	}
	
	/**
	 * Given I have redirected the system out
	 * And I have created two users
	 * And I have cleared the output
	 * When I list users
	 * Then the byte stream contains a single user table
	 * @throws IOException 
	 */
	@Test
	public void testListUsers() throws IOException {
		
		main.createUser("max", "power", "max@power.com", "mp");
		assertThat(outContent.toString(), containsString("max@power.com"));
		assertThat(outContent.toString(), not(containsString("nq@fai.com")));
		int tableCount = StringUtils.countMatches(outContent.toString(), "FIRSTNAME");
		assertThat(tableCount, is(1));
		
		main.createUser("niall", "quinn", "nq@fai.com", "nq");
		assertThat(outContent.toString(), containsString("max@power.com"));
		assertThat(outContent.toString(), containsString("nq@fai.com"));
		tableCount = StringUtils.countMatches(outContent.toString(), "FIRSTNAME");
		assertThat(tableCount, is(2));
		
		clear();
		
		main.listUsers();
		assertThat(outContent.toString(), containsString("max@power.com"));
		assertThat(outContent.toString(), containsString("nq@fai.com"));
		tableCount = StringUtils.countMatches(outContent.toString(), "FIRSTNAME");
		assertThat(tableCount, is(1));
	}	
}
