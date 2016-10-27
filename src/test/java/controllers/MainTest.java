package controllers;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
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
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws Exception {
		  System.setOut(stdout);
	}

	@Test
	public void firstMainTest() {
		System.out.print("hello");
	    assertEquals("hello", outContent.toString());
	}
	
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

}
