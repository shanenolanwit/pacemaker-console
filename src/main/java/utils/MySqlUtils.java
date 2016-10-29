package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.util.Collection;

import models.User;

public class MySqlUtils {

	public final static String NEWLINE = System.getProperty("line.separator");
	public static String createPacemakerScript(Collection<User> users) {
		final StringBuilder scriptBuilder = new StringBuilder("DROP DATABASE IF EXISTS pacemaker;");
		scriptBuilder.append(NEWLINE + "CREATE DATABASE pacemaker;");
		scriptBuilder.append(NEWLINE + "USE pacemaker;");
		scriptBuilder.append(NEWLINE + "SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';");
		scriptBuilder.append(
				NEWLINE + "CREATE TABLE `user` ( `id` BIGINT NOT NULL AUTO_INCREMENT, `firstname` VARCHAR(255), `lastname` VARCHAR(255), `email` VARCHAR(255), `password` VARCHAR(255), PRIMARY KEY  (`id`));");
		scriptBuilder.append(
				NEWLINE + "CREATE TABLE `activity` ( `id` BIGINT NOT NULL AUTO_INCREMENT, `user_id` BIGINT, `type` VARCHAR(255), `location` VARCHAR(255), `distance` DOUBLE, `date` DATETIME, `duration` VARCHAR(255), PRIMARY KEY  (`id`));");
		scriptBuilder.append(
				NEWLINE + "CREATE TABLE `location` ( `id` BIGINT NOT NULL AUTO_INCREMENT, `activity_id` BIGINT, `latitude` DOUBLE, `longitude` DOUBLE, PRIMARY KEY  (`id`));");
		scriptBuilder.append(
				NEWLINE + "ALTER TABLE `activity` ADD CONSTRAINT `activity_fk1` FOREIGN KEY (`user_id`) REFERENCES user(`id`);");
		scriptBuilder.append(
				NEWLINE + "ALTER TABLE `location` ADD CONSTRAINT `location_fk1` FOREIGN KEY (`activity_id`) REFERENCES activity(`id`);");
		users.stream().forEach(u -> {
			StringBuilder sbu = new StringBuilder(NEWLINE + "INSERT INTO USER (ID,FIRSTNAME,LASTNAME,EMAIL,PASSWORD) VALUES(");
			sbu.append(u.id).append(",'").append(u.firstName).append("','").append(u.lastName).append("','")
					.append(u.email).append("','").append(u.password).append("');");
			scriptBuilder.append(sbu.toString());
			final String userID = String.valueOf(u.id);
			u.activities.values().forEach(a -> {
				StringBuilder sba = new StringBuilder(
						NEWLINE + "INSERT INTO ACTIVITY (ID,TYPE,LOCATION,DISTANCE,DATE,DURATION,USER_ID) VALUES(");
				sba.append(a.id).append(",'").append(a.type).append("','").append(a.location).append("',")
						.append(a.distance).append(",'")
						.append(DateTimeUtils.convertLocalDateTimeToString(a.date).replace("T", " ")).append("','")
						.append(DateTimeUtils.convertDurationToString(a.duration)).append("',").append(userID)
						.append(");");
				scriptBuilder.append(sba.toString());
				final long activityID = a.id;
				a.route.forEach(l -> {
					scriptBuilder.append(NEWLINE + "INSERT INTO LOCATION (ID,LATITUDE,LONGITUDE,ACTIVITY_ID) VALUES(" + l.id
							+ "," + l.latitude + "," + l.longitude + "," + activityID + ");");
				});

			});

		});

		return scriptBuilder.toString();
	}

	public static File writeToFile(Collection<User> users) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("pacemaker.sql", false)));
			writer.println(MySqlUtils.createPacemakerScript(users));
			writer.close();
		} catch (IOException ex) {
			System.out.println("Can not write to file");
		}
		return new File("pacemaker.sql");
	}

}
