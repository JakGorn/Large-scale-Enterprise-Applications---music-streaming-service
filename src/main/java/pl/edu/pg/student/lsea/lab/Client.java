package pl.edu.pg.student.lsea.lab;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

/**
 * Class representing the client
 * @author Jakub Górniak, Piotr Cichacki
 */
public class Client extends Thread {

	/** client socket */
    private Socket clientSocket = null;
	/** stream for reading objects from the server */
    private ObjectInputStream in = null;
	/** stream for sending string messages to the server */
    private PrintWriter out = null;
 
    /**
     * Creates new client and establishes connection
     * @param address address of the client socket
     * @param port port of the client socket
     */
    public Client(String address, int port)
    {
        try
        {
            clientSocket = new Socket(address, port);
            System.out.println("Client connected to the server");

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    
    /**
     * Sends message from client to the server.
     * @param msg message to be sent to server
     * @return response from the server
     */
    public Object sendMessage(String msg) {
    	Object resp = null;
    	try
        {
            out.println(msg);
            resp = (Object) in.readObject();
        }
        catch(IOException | ClassNotFoundException i)
        {
            System.out.println(i);
        }
        return resp;
    }
    
    /** Stops the connection of the client with the server */
    public void stopConnection() {
        try
        {
            in.close();
            out.close();
            clientSocket.close();
			System.out.println("Client disconnected from the server.");
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    @SuppressWarnings("unchecked")
	public static void main(String args[])
    {
    	Scanner scanner = new Scanner(System.in);
        Client client = new Client("127.0.0.1", 5555);
        
        String input = "";
    	while(!input.equals("exit"))
    	{
			System.out.println("\nMain menu");
    		System.out.println("You can find information on the song database, type help for more info.");
    		String songName;
    		String stageName;
    		String username;
    		String response;
    		String country;
    		input = scanner.next();
	    	switch (input) {
			case "song":
				System.out.println("Please specify name of the song which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all songs");
				String son = scanner.next();
				songName = son + scanner.nextLine();
				if(songName.equals("all"))
				{		
					// get all songs from server, message = "song all"
					System.out.println((List<Song>) client.sendMessage(input + " " + songName));
				}
				else {
					// get chosen song from server, message = "song 'song name'"
					List<Song> songs = (List<Song>) client.sendMessage(input + " " + songName);
					if(songs != null && !songs.isEmpty())
					{
						songs.forEach(System.out::println);
					}
					else {
						System.out.println("\nSong not found.");
					}
				}
				break;
			case "artist":
				System.out.println("Please specify stage name of the artist which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all artists");
				String sta = scanner.next();
				stageName = sta + scanner.nextLine();
				if(stageName.equals("all"))
				{
					// get all artists from server, message = "artist all"
					System.out.println((List<Artist>) client.sendMessage(input + " " + stageName));
				}
				else {
					// get chosen artist from server, message = "artist 'stage name'"
					List<Artist> artists = (List<Artist>) client.sendMessage(input + " " + stageName);
					if(artists != null && !artists.isEmpty())
					{
						artists.forEach(System.out::println);
					}
					else {
						System.out.println("\nArtist not found.");
					}
				}		
				break;
			case "user":
				System.out.println("Please specify username of the user which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all users");
				String use = scanner.next();
				username = use + scanner.nextLine();
				if(username.equals("all"))
				{
					// get all users from server, message = "user all"
					System.out.println((List<User>) client.sendMessage(input + " " + username));
				}
				else {
					// get chosen user from server, message = "user 'username'"
					List<User> users = (List<User>) client.sendMessage(input + " " + username);
					if(users != null && !users.isEmpty())
					{
						users.forEach(System.out::println);
					}
					else {
						System.out.println("\nUser not found.");
					}
				}
				break;
			case "add":
				System.out.println("Please specify username.");
				String newUse = scanner.next();
				String newUsername = newUse + scanner.nextLine();
				System.out.println("Please specify date of birth (format dd-mm-yyyy).");
				String dat = scanner.next();
				String dateOfBirth = dat + scanner.nextLine();
				System.out.println("Please specify your country.");
				String coun = scanner.next();
				country = coun + scanner.nextLine();
				response = (String) client.sendMessage(input + " " + newUsername + " " + dateOfBirth + " " + country);
				System.out.println(response);
				break;
			case "remove":
				System.out.println("Provide the nickname of a user to be removed.");
				username = scanner.next();
				username = username + scanner.nextLine();
				response = (String) client.sendMessage(input + " " + username);
				System.out.println(response);
				break;
			case "update":
				System.out.println("Which type of data would you like to be updated? (type artist or user)");
				String type = scanner.next();
				type = type + scanner.nextLine();
				String msg;
				if(type.equals("artist")) {
					System.out.println("Please provide the name of an artist to update their info.");
					String stageNameToFind = scanner.next();
					stageNameToFind += scanner.nextLine();
					System.out.println("Please provide new artist stage name.");
					stageName = scanner.next();
					stageName = stageName + scanner.nextLine();
					System.out.println("Please provide new artist country.");
					country = scanner.next();
					country = country + scanner.nextLine();
					System.out.println("Please provide new artist genre.");
					String genre = scanner.next();
					genre = genre + scanner.nextLine();
					msg = input + " " + type + " " + stageNameToFind + ";" + stageName + ";" + country + ";" + genre;
				}
				else if(type.equals("user")) {
					System.out.println("Please provide username of the user to update their info.");
					String usernameToFind = scanner.next();
					usernameToFind += scanner.nextLine();
					System.out.println("Please provide new username.");
					username = scanner.next();
					username = username + scanner.nextLine();
					System.out.println("Please provide new user country.");
					country = scanner.next();
					country = country + scanner.nextLine();
					msg = input + " " + type + " " + usernameToFind + ";" + username + ";" + country;
				} else {
					System.out.println("Please input correct data type.");
					break;
				}
				response = (String) client.sendMessage(msg);
				System.out.println(response);
				break;
			case "help":
				System.out.println("Arguments:");
				System.out.println("help - show help with all possible arguments");
				System.out.println("song - show information about chosen or all songs");
				System.out.println("artist - show information about chosen or all artists");
				System.out.println("user - show information about chosen or all users");
				System.out.println("add - create new user account");
				System.out.println("remove - remove information from the database");
				System.out.println("update - update information in the database");
				System.out.println("exit - exit the app");
				break;
			case "exit":
				input = "exit";
				break;
			default:
				System.out.println("Please input a valid argument. Use help for more information.");
				break;
			}
    	}
    	scanner.close();
    	client.stopConnection();
    }
}
