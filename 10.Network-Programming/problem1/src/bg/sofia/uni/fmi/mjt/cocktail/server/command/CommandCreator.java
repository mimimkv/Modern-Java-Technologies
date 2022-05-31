package bg.sofia.uni.fmi.mjt.cocktail.server.command;


import java.util.Arrays;

public class CommandCreator {

    public static Command newCommand(String clientInput) {
        clientInput = clientInput.replaceAll(System.lineSeparator(), "");
        String[] tokens = clientInput.split(" ");
        String command = tokens[0];

        int index = 1;
        if (command.equals("get")) {
            command = command + " " + tokens[index];
            index++;
        }

        String[] args = Arrays.copyOfRange(tokens, index, tokens.length);
        return new Command(command, args);
    }
}