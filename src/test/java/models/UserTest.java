package models;

import static org.junit.Assert.*;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import exceptions.ValidationException;

import static models.Fixtures.users;
import static models.Fixtures.activities;

public class UserTest {
	
	////FIRST NAME TESTS
	
	@Test
	public void firstNameWithNumbersThrowsException(){
		try{
			new User("d8","wayne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid first name"));
		}
	}
	
	@Test
	public void firstNameWithInvalidSymbolsThrowsException(){
		try{
			new User("b@w","wayne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid first name"));
		}
	}
	
	@Test
	public void firstNameWithInvalidStartThrowsException(){
		try{
			new User("-batman","wayne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid first name"));
		}
	}
	
	@Test
	public void firstNameWithInvalidLengthThrowsException(){
		try{
			new User("","wayne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid first name"));
		}
	}
	
	@Test
	public void firstNameWithValidSymbol(){
		try{
			User u = new User("bruce-batman","wayne","bw@gmail.com","lklk");
			assertThat(u.firstName, is("bruce-batman"));
		} catch(ValidationException e){
			fail("Validation should not have failed");
			
		}
	}
	
	////LAST NAME TESTS
	
	@Test
	public void lastNameWithNumbersThrowsException(){
		try{
			new User("bruce","w4yne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid last name"));
		}
	}
	
	@Test
	public void lastNameWithInvalidSymbolsThrowsException(){
		try{
			new User("bruce","way#ne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid last name"));
		}
	}
	
	@Test
	public void lastNameWithInvalidStartThrowsException(){
		try{
			new User("bruce","'wayne","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid last name"));
		}
	}
	
	@Test
	public void lastNameWithInvalidLengthThrowsException(){
		try{
			new User("bruce","","bw@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid last name"));
		}
	}
	
	@Test
	public void lastNameWithValidSymbol(){
		try{
			User u = new User("bruce","w'ayne","bw@gmail.com","lklk");
			assertThat(u.lastName, is("w'ayne"));
		} catch(ValidationException e){
			fail("Validation should not have failed");
			
		}
	}
	
	
	////EMAIL TESTS
	
	@Test
	public void emailWithInvalidDomainThrowsException(){
		try{
			new User("bruce","wayne","bw@gmail.6","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid email address"));
		}
	}
	
	@Test
	public void emailWithInvalidIdentifierDomainThrowsException(){
		try{
			new User("bruce","wayne","@gmail.com","lklk");
			fail("Validation should have failed");
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid email address"));
		}
	}
	
	
	@Test
	public void emailWithValidSymbol(){
		try{
			User u = new User("bruce","wayne","bw@gmail.com","lklk");
			assertThat(u.email, is("bw@gmail.com"));
		} catch(ValidationException e){
			fail("Validation should not have failed");
			
		}
	}
	
	////PASSWORD TESTS
	@Test
	public void blankPasswordThrowsException(){
		try{
			new User("bruce","wayne","bw@gmail.com","");
			fail("Validation should have failed");
			
		} catch(ValidationException e){
			assertThat(e.getMessage(), is("Invalid password"));
			
		}
	}	

	@Test
	public void testCreate() throws ValidationException {
		User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
		assertThat(homer.firstName, is("homer"));
		assertThat(homer.lastName, is("simpson"));
		assertThat(homer.email, is("homer@simpson.com"));
		assertThat(homer.password, is("secret"));
		
	}

	@Test
	public void testIds() {
		Set<Long> ids = new HashSet<>();
		for (User user : users) {
			ids.add(user.id);
		}
		assertThat(ids.size(), is(users.length));
	}

	@Test
	public void testEquality() {
		Object x = users[0];
		//Comparing identical objects
		assertTrue(users[0].equals(x));
		assertThat(x, is(equalTo(users[0])));
		//Comparing like objects
		assertFalse(users[0].equals(users[1]));
		assertThat(users[1], is(not(equalTo(users[0]))));
		//Comparing unlike objects
		assertThat(new String("Hello World"), is(not(equalTo(users[0]))));
		assertFalse(users[0].equals(new String("Hello World")));
		
		//Testing default constructor and equality
		User userA = new User();
		userA.id = (long) 44;
		userA.firstName = "homer";
		userA.lastName = "simpson";
		userA.email = "homer@simpson.com";
		userA.password = "secret";
		userA.activities.put((long) 20, activities[0]);
		User userB = new User();
		userB.id = (long) 88;
		userB.firstName = "remoh";
		userB.lastName = "nospmis";
		userB.email = "remoh@nospmis.com";
		userB.password = "terces";
		assertThat(userA, is(not(equalTo(userB))));
		userB.firstName = "homer";
		assertThat(userA, is(not(equalTo(userB))));
		userB.lastName = "simpson";
		assertThat(userA, is(not(equalTo(userB))));
		userB.email = "homer@simpson.com";
		assertThat(userA, is(not(equalTo(userB))));
		userB.password = "secret";
		assertThat(userA, is(not(equalTo(userB))));
		userB.activities.put((long) 20, activities[0]);		
		//Testing equals
		assertTrue(userA.equals(userB));
		assertThat(userA, is(equalTo(userB)));
		//Testing hashcode
		assertNotEquals(userA.hashCode(), userB.hashCode());
		assertThat(userA, is(not(sameInstance(userB))));
	}

	@Test
	public void testToString() throws ValidationException {
		User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
		assertEquals("User{" + homer.id + ", homer, simpson, secret, homer@simpson.com, {}}", homer.toString());
	}

}