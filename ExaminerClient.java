import java.io.*;
import java.net.*;

public class ExaminerClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 12345);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        String name = consoleInput.readLine();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Invalid name. Exiting.");
            socket.close();
            return;
        }
        out.println(name); // Inform the server of the examiner's name

        while (true) {
            String question = in.readLine(); // Read the question from the server
            if (question == null || question.equalsIgnoreCase("END")) break; // Exit when quiz ends

            System.out.println(question); // Display the question and options to the user
            System.out.print("Enter your answer (A/B/C/D): ");
            String answer = consoleInput.readLine(); // Read the user's answer
            out.println(answer); // Send the answer to the server
        }

        socket.close(); // Close the socket after the quiz
    }
}
