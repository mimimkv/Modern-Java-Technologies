package bg.sofia.uni.fmi.mjt.cocktail.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class CocktailClient {

    private static final int SERVER_PORT = 7777;

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, "UTF-8"));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, "UTF-8"), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress("localhost", SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("=> ");
                String message = scanner.nextLine(); // read a line from the console

                if ("disconnect".equals(message)) {
                    System.out.println("=> Disconnected from server");
                    break;
                }

                writer.println(message);

                String reply = reader.readLine(); // read the response from the server
                System.out.println("=> " + reply);
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
