import java.io.*;
import java.net.*;

public class AdminClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 12345);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        out.println("Admin"); // Inform the server that this is the admin

        while (true) {
            System.out.println("Enter questions (format: question;A;B;C;D;CorrectOption). Type 'done' when finished:");
            StringBuilder questions = new StringBuilder();
            String question;
            while (!(question = consoleInput.readLine()).equalsIgnoreCase("done")) {
                questions.append(question).append("\n");
            }

            out.println(questions.toString()); // Send all questions to the server

            System.out.println("Questions sent to the server. Type 'exit' to quit or add more questions.");
            if (consoleInput.readLine().equalsIgnoreCase("exit")) {
                break; // Exit the loop if "exit" is entered
            }
        }

        socket.close(); // Close the socket when done
    }
}
