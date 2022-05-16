package pl.edu.pg.student.lsea.lab;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    /**
     * method that starts the server on a particular port
     * @param port - port number of the server
     * @throws IOException
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    /**
     * method used to stop the server and close server socket
     * @throws IOException
     */
    public void stop() throws IOException {
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

        /**
         * constructor
         * @param socket client's socket
         */
        public ClientHandler(Socket socket) {
            try {
                this.clientSocket = socket;
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new ObjectOutputStream(clientSocket.getOutputStream());
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
                while ((message = in.readLine()) != null) {
                    System.out.println("Request from client side: " + message);
                    String messageArray[] = message.split(" ", 2);
                    switch (messageArray[0]) {
                        case "song":
                            String songName = messageArray[1];
                            if (songName.equals("all")) {
                                out.writeObject(data.getSongs());
                            } else {
                                Song song = data.getSongs().stream()
                                        .filter(s -> songName.equals(s.getName()))
                                        .findAny()
                                        .orElse(null);
                                out.writeObject(song);
                            }
                            break;
                        case "artist":
                            String stageName = messageArray[1];
                            if (stageName.equals("all")) {
                                out.writeObject(data.getArtists());
                            } else {
                                Artist artist = data.getArtists().stream()
                                        .filter(a -> stageName.equals(a.getStageName()))
                                        .findAny()
                                        .orElse(null);
                                out.writeObject(artist);
                            }
                            break;
                        case "user":
                            String username = messageArray[1];
                            if (username.equals("all")) {
                                out.writeObject(data.getUsers());
                            } else {
                                User user = data.getUsers().stream()
                                        .filter(u -> username.equals(u.getUsername()))
                                        .findAny()
                                        .orElse(null);
                                out.writeObject(user);
                            }
                            break;
                        default:
                            break;
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Client has disconnected from the server.");
            } catch (Exception e) {
                try {
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
