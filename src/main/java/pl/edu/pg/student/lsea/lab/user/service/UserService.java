package pl.edu.pg.student.lsea.lab.user.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.user.User;

/**
 * Class for User object service
 * @author Jakub Górniak
 */
public class UserService {

	/** database handler for user objects */
	private DatabaseHandler<User> userHandler;
	
	/** formatter for transforming string type to date type */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
	
	/**
	 * constructor
	 * @param userHandler database handler for user objects
	 */
	public UserService(DatabaseHandler<User> userHandler) {
		this.userHandler = userHandler;
	}
	
	/**
     * Method for adding new user to the database
     * @param newUserData data of new user
     * @param userHandler database handler for the user objects
     * @throws IOException
     * @return result of the add operation
     */
    public String addUser(String[] newUserData) throws IOException {
        String newUsername = newUserData[0];
        LocalDate dateOfBirth = null;
        try {
        	dateOfBirth = LocalDate.parse(newUserData[1], formatter);
        } catch (DateTimeParseException e) {
        	return "\nAdding user failed. Invalid date format provided.";
		}
        String country = newUserData[2];
        User newUser = new User(newUsername, dateOfBirth, country);
        userHandler.save_to_database(newUser);
        return "\nNew user added.";
    }
    
    /**
     * method for getting user from the database with given username
     * @param username username of the user
     * @return list of all users with given username
     */
    public List<User> getUser(String username) {
    	return this.userHandler.get_from_database("user", username);
    }
    
    /**
     * method for updating the chosen user in the database
     * @param userData updated data of the user
     * @return result of the update operation
     */
    public String updateUser(String[] userData) {
    	String username = userData[0];
    	List<User> users = getUser(username);
        if (!users.isEmpty()) {
        	User user = users.get(0);
            userHandler.update_to_database("user", userData, user);
    		return ("\nUser " + username + " updated");
        } else {
        	return "\nUser not found";
        }
    }
    
    /**
     * method for removing the chosen user from database
     * @param username username of the user
     * @return result of the remove operation
     */
    public String removeUser(String username) {
        List<User> users = getUser(username);
        if (users.isEmpty())
        {
        	return "\nUser not found";
        }
		for(User user : users) {
			userHandler.remove_from_database(user);
		}
		return ("\nUser " + username + " removed");
    }
}
