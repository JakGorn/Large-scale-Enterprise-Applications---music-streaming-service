package pl.edu.pg.student.lsea.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.configuration.DataInitializer;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

public class Server {
    private static DataInitializer data = new DataInitializer();
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    // String messageArray[] = message.split(" (?=(?:[^']*'[^']*')*[^']*$)");
                    String messageArray[] = message.split(" ", 2);
                    switch (messageArray[0]) {
                        case "song":
                            String songName = messageArray[1];
                            if (songName.equals("all")) {
                                // send message to client containing all songs
                            } else {
                                Song song = data.getSongs().stream()
                                        .filter(s -> songName.equals(s.getName()))
                                        .findAny()
                                        .orElse(null);
                                if (song != null) {
                                    // send message about particular song
                                } else {
                                    // send message to print out "song not found"
                                }
                            }
                            break;
                        case "artist":
                            String stageName = messageArray[1];
                            if (stageName.equals("all")) {
                                // send message to client containing data about all artists
                            } else {
                                Artist artist = data.getArtists().stream()
                                        .filter(a -> stageName.equals(a.getStageName()))
                                        .findAny()
                                        .orElse(null);
                                if (artist != null) {
                                    // send message about particular artist
                                } else {
                                    // send message to print out "artist not found"
                                }
                            }
                            break;
                        case "user":
                            String username = messageArray[1];
                            if (username.equals("all")) {
                                // send message about all users
                            } else {
                                User user = data.getUsers().stream()
                                        .filter(u -> username.equals(u.getUsername()))
                                        .findAny()
                                        .orElse(null);
                                if (user != null) {
                                    // send message about particular user
                                } else {
                                    // send message to print out "user not found"
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
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
