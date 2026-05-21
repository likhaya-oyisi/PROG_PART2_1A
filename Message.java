package chatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Message class for the QuickChat application. Handles message creation, validation, sending, storing, and display.
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;

    // Shared state across all instances for this session
    private static final List<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;
    private static int messageCounter    = 0;


    public Message(String recipientCell, String messageText) {
        this.recipientCell = recipientCell;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageNumber = ++messageCounter;
        this.messageHash   = createMessageHash();
    }
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipientCell == null || !recipientCell.matches("^\\+\\d{1,10}$")) {
            return "Cell phone number is incorrectly formatted or does not contain " +
                    "an international code. Please correct the number and try again.";
        }
        return "Cell phone number successfully captured.";
    }

    public String createMessageHash() {
        String idPrefix   = messageID.substring(0, 2);
        String firstWord  = "";
        String lastWord   = "";

        if (messageText != null && !messageText.trim().isEmpty()) {
            String[] words = messageText.trim().split("\\s+");
            firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
            lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        }

        this.messageHash = (idPrefix + ":" + (messageNumber - 1) + ":" + firstWord + lastWord)
                .toUpperCase();
        return this.messageHash;
    }

    public String checkMessageLength() {
        if (messageText == null || messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = messageText.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder("=== Sent Messages ===\n");
        for (Message m : sentMessages) {
            sb.append("Message ID:   ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient:    ").append(m.recipientCell).append("\n");
            sb.append("Message:      ").append(m.messageText).append("\n");
            sb.append("---------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        String json = "{"
                + "\"messageID\":\"" + messageID + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"recipient\":\"" + recipientCell + "\","
                + "\"message\":\"" + messageText.replace("\"", "\\\"") + "\","
                + "\"messageHash\":\"" + messageHash + "\""
                + "}";

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json);
            file.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    public static void resetState() {
        sentMessages.clear();
        totalMessagesSent = 0;
        messageCounter    = 0;
    }

    // Getters
    public String getMessageID()     { return messageID; }
    public int    getMessageNumber() { return messageNumber; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageText()   { return messageText; }
    public String getMessageHash()   { return messageHash; }
}
