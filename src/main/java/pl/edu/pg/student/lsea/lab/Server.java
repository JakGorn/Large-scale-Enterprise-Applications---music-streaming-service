package pl.edu.pg.student.lsea.lab;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.persistence.*;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.artist.service.ArtistService;
import pl.edu.pg.student.lsea.lab.configuration.DataInitializer;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.song.service.SongService;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.service.UserService;

/**
 * Class representing the server
 * @author Jan Bogdziewicz, Piotr Cichacki, Jakub Górniak
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
        @Override
        public void run() {
            try {
                String message;
                SongService songService = new SongService(new DatabaseHandler<Song>(this.em, Song.class));
                ArtistService artistService = new ArtistService(new DatabaseHandler<Artist>(this.em, Artist.class));
                UserService userService = new UserService(new DatabaseHandler<User>(this.em, User.class));

                while ((message = in.readLine()) != null) {
                    System.out.println("Request from client side: " + message);
                    String messageArray[] = message.split(" ", 2);
                    switch (messageArray[0]) {
                        case "song":
                            String songName = messageArray[1];
                            List<Song> songQueryResult = songService.getSong(songName);
                            out.writeObject(songQueryResult);
                            break;
                        case "artist":
                            String stageName = messageArray[1];
                            List<Artist> artistQueryResult = artistService.getArtist(stageName);
                            out.writeObject(artistQueryResult);
                            break;
                        case "user":
                            String username = messageArray[1];
                            List<User> userQueryResult = userService.getUser(username);
                            out.writeObject(userQueryResult);
                            break;
                        case "add":                   
                        	out.writeObject(userService.addUser(messageArray[1].split(" ")));
                            break;
                        case "remove":
                        	out.writeObject(userService.removeUser(messageArray[1]));
                    		break;                    	
                        case "update":
                        	String objectTypeAndID[] = messageArray[1].split(" ", 2);
                        	String type = objectTypeAndID[0];
                        	String[] info = objectTypeAndID[1].split(";");
                        	switch(type) {
                        	case "artist":
                        		out.writeObject(artistService.updateArtist(info));
                        		break;
                        	case "user":
                        		out.writeObject(userService.updateUser(info));
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
