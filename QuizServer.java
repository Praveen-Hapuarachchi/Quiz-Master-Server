import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static Map<String, Integer> scores = new HashMap<>();
    private static List<Question> questions = new ArrayList<>();
    private static List<Socket> examiners = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started on port 12345");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept incoming client connection
                new Thread(new ClientHandler(clientSocket)).start(); // Handle each client in a new thread
            }
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                String clientType = in.readLine(); // First message determines client type

                if (clientType.equals("Admin")) {
                    handleAdmin();
                } else {
                    handleExaminer(clientType);
                }
            } catch (IOException e) {
                System.err.println("Client connection error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close(); // Ensure the socket is closed when the client disconnects
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void handleAdmin() throws IOException {
            System.out.println("Admin connected.");
            while (true) {
                String questionData = in.readLine();
                if (questionData == null || questionData.equalsIgnoreCase("exit")) break;

                try {
                    String[] parts = questionData.split(";");
                    if (parts.length != 6) {
                        out.println("Error: Invalid question format. Use format: question;A;B;C;D;CorrectOption");
                        continue;
                    }

                    Question question = new Question(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    questions.add(question);
                    System.out.println("Question added: " + question.question);
                    out.println("Question added successfully!");

                    // Send the question to all connected examiners
                    sendQuestionToExaminers(question);
                } catch (Exception e) {
                    out.println("Error adding question: " + e.getMessage());
                }
            }
        }

        private void handleExaminer(String examinerName) throws IOException {
            if (examinerName == null || examinerName.trim().isEmpty()) {
                System.out.println("Invalid examiner name. Disconnecting client.");
                return;
            }

            System.out.println("Examiner connected: " + examinerName);
            scores.put(examinerName, 0); // Initialize the examiner's score
            examiners.add(clientSocket); // Add the examiner to the list of connected clients

            for (Question question : questions) {
                // Send the question and all options to the examiner as one line
                out.println(question.toString());
                out.println("Enter your answer (A/B/C/D):");

                String answer = in.readLine(); // Read the examiner's answer
                if (answer == null || answer.isEmpty()) {
                    out.println("Invalid answer. Moving to the next question.");
                    continue;
                }

                // Validate the answer
                boolean isCorrect = question.correct.equalsIgnoreCase(answer.trim());
                if (isCorrect) {
                    scores.put(examinerName, scores.get(examinerName) + 1); // Update the score
                }

                // Send the result back to the examiner
                String resultMessage = examinerName + " gave answer as " + answer.toUpperCase() + ") " + getAnswerOption(answer) + 
                    (isCorrect ? " - Correct!" : " - Incorrect.");
                out.println(resultMessage); // Send feedback about correctness

                // Log the result on the server's side
                System.out.println(resultMessage);

                // Send updated leaderboard to all examiners
                sendLeaderboard();
            }

            out.println("END"); // Notify the examiner that the quiz has ended
        }

        private void sendQuestionToExaminers(Question question) {
            String questionMessage = question.toString();
            Iterator<Socket> iterator = examiners.iterator();
            while (iterator.hasNext()) {
                Socket examiner = iterator.next();
                try {
                    PrintWriter examinerOut = new PrintWriter(examiner.getOutputStream(), true);
                    examinerOut.println(questionMessage); // Send the question to each examiner
                } catch (IOException e) {
                    System.err.println("Error sending question to examiner: " + e.getMessage());
                    iterator.remove(); // Remove disconnected examiner
                }
            }
        }

        private void sendLeaderboard() {
            StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                leaderboard.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            Iterator<Socket> iterator = examiners.iterator();
            while (iterator.hasNext()) {
                Socket examiner = iterator.next();
                try {
                    PrintWriter examinerOut = new PrintWriter(examiner.getOutputStream(), true);
                    examinerOut.println(leaderboard.toString()); // Send the leaderboard to each examiner
                } catch (IOException e) {
                    System.err.println("Error sending leaderboard: " + e.getMessage());
                    iterator.remove(); // Remove disconnected examiner
                }
            }
        }

        private String getAnswerOption(String answer) {
            switch (answer.toUpperCase()) {
                case "A": return "Paris";
                case "B": return "London";
                case "C": return "Berlin";
                case "D": return "Madrid";
                default: return "Invalid Answer";
            }
        }
    }

    static class Question {
        String question, optionA, optionB, optionC, optionD, correct;

        public Question(String question, String optionA, String optionB, String optionC, String optionD, String correct) {
            this.question = question;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correct = correct;
        }

        @Override
        public String toString() {
            // This formats the options in a single row for easier viewing
            return question + "\tA) " + optionA + " ; B) " + optionB + " ; C) " + optionC + " ; D) " + optionD;
        }
    }
}
