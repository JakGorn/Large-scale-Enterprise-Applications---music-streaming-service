package pl.edu.pg.student.lsea.lab;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.*;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.configuration.DataInitializer;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

/**
 * Class representing the server
 * @author Jan Bogdziewicz, Piotr Cichacki
 */
public class Server {
    /** data generator */
    private static DataInitializer data = new DataInitializer();
    /** server's socket */
    private ServerSocket serverSocket;
    /** entity manage factory */
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("name_PU");


    /**
     * method that starts the server on a particular port
     * @param port - port number of the server
     * @throws IOException
     */
    public void start(int port) throws IOException {
    	initDatabase();
        serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (true) {
            new ClientHandler(serverSocket.accept(), this.emf).start();
        }
    }

    /**
     * Initializes the database with generated data
     */
    private void initDatabase() {
    	
    	EntityManager em = emf.createEntityManager();
    	EntityTransaction tx = em.getTransaction();

        DatabaseHandler<Artist> artistHandler = new DatabaseHandler<Artist>(em, Artist.class);
        DatabaseHandler<User> userHandler = new DatabaseHandler<User>(em, User.class);
    	
    	for(Artist a : data.getArtists())
        {
            artistHandler.save_to_database(a);
        }
        for(User u : data.getUsers())
        {
        	userHandler.save_to_database(u);
        }
	}

	/**
     * method used to stop the server and close server socket
     * @throws IOException
     */
    public void stop() throws IOException {
    	emf.close();
        serverSocket.close();
        System.out.println("Server disconnected");
    }

    /**
     * static class that handles connected client
     * @author Jan Bogdziewicz, Piotr Cichacki
     */
    private static class ClientHandler extends Thread {
        /** client socket */
        private Socket clientSocket;
        /** stream for sending objects to the client */
        private ObjectOutputStream out;
        /** stream for reading string messages from the client */
        private BufferedReader in;
        /** entity manager */
        private EntityManager em;
        /** formater for transforming string type to date type */
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");

        /**
         * constructor
         * @param socket client's socket
         * @param emf entity manager factory
         */
        public ClientHandler(Socket socket, EntityManagerFactory emf) {
            try {
                this.clientSocket = socket;
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new ObjectOutputStream(clientSocket.getOutputStream());
                this.em = emf.createEntityManager();
                System.out.println("Client has connected to the server");
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        /**
         * thread's method to be run when the thread starts
         */
        @SuppressWarnings("unchecked")
		@Override
        public void run() {
            try {
                String message;
                DatabaseHandler<Song> songHandler = new DatabaseHandler<Song>(this.em, Song.class);
                DatabaseHandler<Artist> artistHandler = new DatabaseHandler<Artist>(this.em, Artist.class);
                DatabaseHandler<User> userHandler = new DatabaseHandler<User>(this.em, User.class);

                while ((message = in.readLine()) != null) {
                    System.out.println("Request from client side: " + message);
                    String messageArray[] = message.split(" ", 2);
                    switch (messageArray[0]) {
                        case "song":
                            String songName = messageArray[1];
                            List<Song> songQueryResult = songHandler.get_from_database("song", songName);
                            out.writeObject(songQueryResult);
                            break;
                        case "artist":
                            String stageName = messageArray[1];
                            List<Artist> artistQueryResult = artistHandler.get_from_database("artist", stageName);
                            out.writeObject(artistQueryResult);
                            break;
                        case "user":
                            String username = messageArray[1];
                            List<User> userQueryResult = userHandler.get_from_database("user", username);
                            out.writeObject(userQueryResult);
                            break;
                        case "add":
                            try {
                                String[] newUserData = messageArray[1].split(" ");
                                String newUsername = newUserData[0];
                                LocalDate dateOfBirth = LocalDate.parse(newUserData[1], formatter);
                                String country = newUserData[2];
                                User newUser = new User(newUsername, dateOfBirth, country);
                                userHandler.save_to_database(newUser);
                                out.writeObject("\nNew user added.");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                out.writeObject("\nAdding user failed.");
                            }
                            break;
                        case "remove":
                        	try {
	                        	username = messageArray[1];
                                List<User> users = userHandler.get_from_database("user", username);
	                    		for(User user : users) {
	                    			userHandler.remove_from_database(user);
	                    		}
	                    		String msg = "\nUser " + username + " removed";
	                    		out.writeObject(msg);
                        	} catch (Exception e) {
                        		System.out.println(e.getMessage());
                                out.writeObject("\nRemoving user failed.");
                        	}
                    		break;                    	
                        case "update":
                        	String objectTypeAndID[] = messageArray[1].split(" ", 2);
                        	String type = objectTypeAndID[0];
                        	String[] info = objectTypeAndID[1].split(";");
                        	switch(type) {
                        	case "artist":
                                List<Artist> artists = artistHandler.get_from_database("artist", info[0]);
                                if (!artists.isEmpty()) {
                                	Artist artist = artists.get(0);
                            		artistHandler.update_to_database("artist", info, artist);
                            		out.writeObject("\nArtist " + info[0] + " updated");
                                } else {
                                	out.writeObject("\nArtist not found");
                                }
                        		break;
                        	case "user":
                                List<User> users = userHandler.get_from_database("user", info[0]);
                                if (!users.isEmpty()) {
                                	User user = users.get(0);
                                    userHandler.update_to_database("user", info, user);
                            		out.writeObject("\nUser " + info[0] + " updated");
                                } else {
                                	out.writeObject("\nUser not found");
                                }
                        		break;
                        	default:
                        		break;
                        	}
                        	break;
                        default:
                            break;
                    }
                }
                em.close();
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Client has disconnected from the server.");
            } catch (Exception e) {
                try {
                	em.close();
                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("Client has disconnected from the server.");
                } catch (IOException i) {
                    System.out.println(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(5555);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
