package pl.edu.pg.student.lsea.lab;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

/**
 * Class representing the client
 * @author Jakub Górniak
 */
public class Client extends Thread {

    private Socket clientSocket = null;
    private ObjectInputStream in = null;
    private DataOutputStream out = null;
 
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
            System.out.println("Connected");

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
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
            out.writeUTF(msg);
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
        Client client = new Client("127.0.0.1", 5000);
        
        String input = "";
    	while(!input.equals("exit"))
    	{
			System.out.println("Main menu");
    		System.out.println("You can find information on the song database, type help for more info.");
    		input = scanner.next();
	    	switch (input) {
			case "song":
				System.out.println("Please specify name of the song which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all songs");
				String son = scanner.next();
				String songName = son + scanner.nextLine();
				if(songName.equals("all"))
				{		
					// get all songs from server, message = "song all"
					System.out.println((List<Song>) client.sendMessage(input + songName));
				}
				else {
					// get chosen song from server, message = "song 'song name'"
					Song song = (Song) client.sendMessage(input + songName);
					/** looking for certain song server side
					Song song = data.getSongs().stream()
							  .filter(s -> songName.equals(s.getName()))
							  .findAny()
							  .orElse(null); */
					if(song != null)
					{
						System.out.println(song);
					}
					else {
						System.out.println("Song not found.");
					}
				}
				break;
			case "artist":
				System.out.println("Please specify stage name of the artist which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all artists");
				String sta = scanner.next();
				String stageName = sta + scanner.nextLine();
				if(stageName.equals("all"))
				{
					// get all artists from server, message = "artist all"
					System.out.println((List<Artist>) client.sendMessage(input + stageName));
				}
				else {
					// get chosen artist from server, message = "artist 'stage name'"
					Artist artist = (Artist) client.sendMessage(input + stageName);
					/** looking for certain artist server side
					Artist artist = data.getArtists().stream()
						  .filter(a -> stageName.equals(a.getStageName()))
						  .findAny()
						  .orElse(null); */
					if(artist != null)
					{
						System.out.println(artist);
					}
					else {
						System.out.println("Artist not found.");
					}
				}		
				break;
			case "user":
				System.out.println("Please specify username of the user which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all users");
				String use = scanner.next();
				String username = use + scanner.nextLine();
				if(username.equals("all"))
				{
					// get all users from server, message = "user all"
					System.out.println((List<User>) client.sendMessage(input + username));
				}
				else {
					// get chosen user from server, message = "user 'username'"
					User user = (User) client.sendMessage(input + username);
					/** looking for certain user server side
					User user = data.getUsers().stream()
							  .filter(u -> username.equals(u.getUsername()))
							  .findAny()
							  .orElse(null); */
					if(user != null)
					{
						System.out.println(user);
					}
					else {
						System.out.println("User not found.");
					}
				}
				break;
			case "help":
				System.out.println("Arguments:");
				System.out.println("help - show help with all possible arguments");
				System.out.println("song - show information about chosen or all songs");
				System.out.println("artist - show information about chosen or all artists");
				System.out.println("user - show information about chosen or all users");
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