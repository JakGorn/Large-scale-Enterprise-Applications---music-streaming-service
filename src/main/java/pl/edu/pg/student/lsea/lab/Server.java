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

    //test

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
    	
    	for(Artist a : data.getArtists())
        {
        	tx.begin();
        	em.persist(a);
        	tx.commit();
        }
        for(User u : data.getUsers())
        {
        	tx.begin();
        	em.persist(u);
        	tx.commit();
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
                while ((message = in.readLine()) != null) {
                    System.out.println("Request from client side: " + message);
                    String messageArray[] = message.split(" ", 2);
                    //String objectTypeAndID[];
                    String username;
                    switch (messageArray[0]) {
                        case "song":
                            String songName = messageArray[1];
                            if (songName.equals("all")) { 
                                out.writeObject(this.em.createNamedQuery("findAllSongs", Song.class).getResultList());
                            } else {
                                Query query = this.em.createNamedQuery("findSong_byName", Song.class);
                                query.setParameter("name", songName);
                                out.writeObject(query.getResultList());
                            }
                            break;
                        case "artist":
                            String stageName = messageArray[1];
                            if (stageName.equals("all")) {
                                out.writeObject(this.em.createNamedQuery("findAllArtists", Artist.class).getResultList());
                            } else {
                                Query query = this.em.createNamedQuery("findArtist_byStageName", Artist.class);
                                query.setParameter("stageName", stageName);
                                out.writeObject(query.getResultList());
                            }
                            break;
                        case "user":
                            username = messageArray[1];
                            if (username.equals("all")) {
                                out.writeObject(this.em.createNamedQuery("findAllUsers", User.class).getResultList());
                            } else {
                                Query query = this.em.createNamedQuery("findUser_byUsername", User.class);
                                query.setParameter("username", username);
                                out.writeObject(query.getResultList());
                            }
                            break;
                        case "add":
                            try {
                                String newUserData[] = messageArray[1].split(" ");
                                EntityTransaction tx = this.em.getTransaction();
                                String newUsername = newUserData[0];
                                LocalDate dateOfBirth = LocalDate.parse(newUserData[1], formatter);
                                String country = newUserData[2];
                                User newUser = new User(newUsername, dateOfBirth, country);
                                tx.begin();
                                em.persist(newUser);
                                tx.commit();
                                out.writeObject("\nNew user added.");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                out.writeObject("\nAdding user failed.");
                            }
                            break;
                        case "remove":
                        	try {
	                        	username = messageArray[1];
	                        	EntityTransaction tx = this.em.getTransaction();
	                        	
	                        	Query query = this.em.createNamedQuery("findUser_byUsername", User.class);
	                            query.setParameter("username", username);
	                    		List<User> users = (List<User>) query.getResultList();
	                    		for(User user : users) {
	                    			tx.begin();
	                        		this.em.remove(user);
	                        		tx.commit();
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
                        	EntityTransaction tx = this.em.getTransaction();
                        	Query query;
                        	String msg;
                        	switch(type) {
                        	case "artist":
                        		query = this.em.createNamedQuery("findArtist_byStageName", Artist.class);
                                query.setParameter("stageName", info[0]);
                                List<Artist> artists = query.getResultList();
                                if (!artists.isEmpty()) {
                                	Artist artist = artists.get(0);
                            		tx.begin();
                            		artist.setStageName(info[1]);
                            		artist.setCountry(info[2]);
                            		artist.setGenre(info[3]);
                            		tx.commit();
                            		this.em.clear();
                            		msg = "\nArtist " + info[0] + " updated";
                            		out.writeObject(msg);
                                } else {
                                	out.writeObject("\nArtist not found");
                                }
                        		break;
                        	case "user":
                        		query = this.em.createNamedQuery("findUser_byUsername", User.class);
                                query.setParameter("username", info[0]);
                                List<User> users = query.getResultList();
                                if (!users.isEmpty()) {
                                	User user = users.get(0);
                            		tx.begin();
                            		user.setUsername(info[1]);
                            		user.setCountry(info[2]);
                            		tx.commit();
                            		this.em.clear();
                            		msg = "\nUser " + info[0] + " updated";
                            		out.writeObject(msg);
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
