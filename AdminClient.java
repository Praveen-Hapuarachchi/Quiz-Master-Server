import java.io.*;
import java.net.*;

public class AdminClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 12345);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        out.println("Admin"); // Inform the server that this is the admin

        while (true) {
            System.out.println("Enter question (format: question;A;B;C;D;CorrectOption):");
            String question = consoleInput.readLine();

            if (question.equalsIgnoreCase("exit")) {
                break; // Exit the loop if "exit" is entered
            }

            out.println(question); // Send the question to the server
        }

        socket.close(); // Close the socket when done
    }
}
