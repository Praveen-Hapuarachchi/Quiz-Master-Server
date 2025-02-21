import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminClient extends JFrame {

    private JTextArea questionTextArea;
    private JButton sendButton;
    private JButton doneButton;
    private JTextArea outputArea; // For server feedback
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;  // To read messages from the server

    public AdminClient() {
        super("Admin Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // 1. Create and Add Components

        questionTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        sendButton = new JButton("Send Questions");
        doneButton = new JButton("Done");
        buttonPanel.add(sendButton);
        buttonPanel.add(doneButton);
        add(buttonPanel, BorderLayout.SOUTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        add(outputScrollPane, BorderLayout.NORTH);

        // 2. Set up Event Listeners

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendQuestionsToServer();
            }
        });

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendDoneToServer();
            }
        });

        // 3. Connect to the Server (in constructor)

        try {
            socket = new Socket("127.0.0.1", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Initialize 'in'
            out.println("Admin"); // Identify as admin

            // Start a thread to listen for server messages
            new Thread(this::receiveServerMessages).start();

        } catch (IOException ex) {
            outputArea.append("Error connecting to server: " + ex.getMessage() + "\n");
            sendButton.setEnabled(false); // Disable the send button if connection fails
            doneButton.setEnabled(false); // Disable the done button if connection fails
        }

        setVisible(true);
    }

    private void sendQuestionsToServer() {
        String questions = questionTextArea.getText();
        out.println(questions);
        questionTextArea.setText(""); // Clear the input area
    }

    private void sendDoneToServer() {
        out.println("DONE");
        outputArea.append("Admin added all questions to the quiz.\n");
    }

    private void receiveServerMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("FINAL_SCORES|")) {
                    String finalScores = message.substring(13); // Remove "FINAL_SCORES|" prefix
                    outputArea.append("Final Scores:\n" + finalScores + "\n");
                } else {
                    outputArea.append(message + "\n");
                }
            }
        } catch (IOException e) {
            outputArea.append("Error reading from server: " + e.getMessage() + "\n");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                outputArea.append("Error closing socket: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminClient::new);
    }
}