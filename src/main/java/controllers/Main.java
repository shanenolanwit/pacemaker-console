package controllers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import java.io.File;
import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import models.Activity;
import models.User;
import utils.DateTimeUtils;
import utils.Serializer;
import utils.XMLSerializer;

public class Main {
	PacemakerAPI paceApi;

	@Command(description = "Create a new User")
	public void createUser(
			@Param(name = "first name") String firstName, 
			@Param(name = "last name") String lastName,
			@Param(name = "email") String email, 
			@Param(name = "password") String password) {
		paceApi.createUser(firstName, lastName, email, password);
	}

	@Command(description = "Get a Users details")
	public void getUser(@Param(name = "email") String email) {
		Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
		if (user.isPresent()) {
			System.out.println(user);
		}	
	}

	@Command(description = "Get all users details")
	public void getUsers() {
		List<User> users = new ArrayList<User>(paceApi.getUsers());
		if (!users.isEmpty()) {
			V2_AsciiTable at = new V2_AsciiTable();
			at.addRule();
			at.addRow("ID", "First Name", "Last Name", "Email", "Password");
			at.addStrongRule();

			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				at.addRow(user.id.toString(), user.firstName, user.lastName, user.email, user.password);
				at.addRule();
			}
			V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
			rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
			rend.setWidth(new WidthAbsoluteEven(76));
			RenderedTable rt = rend.render(at);
			System.out.println(rt);
		} else {
			System.out.println("No users found");
		}
	}

	@Command(description = "Delete a User")
	public void deleteUser(@Param(name = "email") String email) {
		Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
		if (user.isPresent()) {
			paceApi.deleteUser(user.get().id);
		}
	}

	@Command(description = "Add an activity")
	public void addActivity(@Param(name = "user-id") Long id, @Param(name = "type") String type,
			@Param(name = "location") String location, @Param(name = "distance") double distance,
			@Param(name = "date") String date) {

		Optional<User> user = Optional.fromNullable(paceApi.getUser(id));
		if (user.isPresent() && DateTimeUtils.isValidDate(date)) {
			paceApi.createActivity(id, type, location, distance, DateTimeUtils.convertStringToLocalDateTime(date));
		}
	}

	@Command(description = "Add a location to an activity")
	public void addLocation(@Param(name = "activity-id") Long id, @Param(name = "longitude") double longitude,
			@Param(name = "latitude") double latitude) {
		Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(id));
		if (activity.isPresent()) {
			paceApi.addLocation(activity.get().id, latitude, longitude);
		}

	}

	public Main() throws Exception {
		// XML Serializer
		
		Serializer serializer = new XMLSerializer();

		// JSON Serializer
		// File datastore = new File("datastore.json");
		// Serializer serializer = new JSONSerializer(datastore);

		// Binary Serializer
		// File datastore = new File("datastore.txt");
		// Serializer serializer = new BinarySerializer(datastore);

		paceApi = new PacemakerAPI(serializer);
		paceApi.load();
		
	}

	public static void main(String[] args) throws Exception {
		Main main = new Main();

		Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions",
				main);
		shell.commandLoop();

		main.paceApi.store();
	}

}