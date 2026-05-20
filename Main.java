package chatapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===========================================");
        System.out.println("   Welcome to QuickChat");
        System.out.println("===========================================");
        System.out.println("\n--- REGISTRATION ---");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (must contain '_' and be max 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (min 8 chars, 1 capital, 1 number, 1 special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter cell phone number (e.g. +27838968976): ");
        String cellPhone = scanner.nextLine();

        chatapp.Login registration = new chatapp.Login(username, password, cellPhone, firstName, lastName);
        String regResult = registration.registerUser();
        System.out.println("\n" + regResult);

        if (!regResult.contains("Registration successful")) {
            System.out.println("Please restart and try again.");
            scanner.close();
            return;
        }

        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter username: ");
        String loginUser = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();

        chatapp.Login loginAttempt = new chatapp.Login(loginUser, loginPass, "", "", "");
        String loginStatus = loginAttempt.returnLoginStatus();
        System.out.println("\n" + loginStatus);

        if (!loginAttempt.loginUser()) {
            System.out.println("Login failed. Exiting.");
            scanner.close();
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        System.out.print("\nHow many messages would you like to send? ");
        int maxMessages = 0;
        try {
            maxMessages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Exiting.");
            scanner.close();
            return;
        }

        int messagesSentThisSession = 0;

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (messagesSentThisSession >= maxMessages) {
                        System.out.println("You have reached your message limit of " + maxMessages + ".");
                        break;
                    }

                    System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                    String recipient = scanner.nextLine().trim();

                    System.out.print("Enter your message: ");
                    String text = scanner.nextLine();

                    chatapp.Message msg = new chatapp.Message(recipient, text);

                    String cellCheck = msg.checkRecipientCell();
                    System.out.println(cellCheck);
                    if (!cellCheck.contains("successfully")) {
                        System.out.println("Message cancelled due to invalid recipient.");
                        break;
                    }

                    String lengthCheck = msg.checkMessageLength();
                    System.out.println(lengthCheck);
                    if (!lengthCheck.equals("Message ready to send.")) {
                        System.out.println("Please re-enter your message.");
                        break;
                    }

                    System.out.println("\nMessage ID generated: " + msg.getMessageID());
                    System.out.println("Message Hash:         " + msg.getMessageHash());

                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message to send later");
                    System.out.print("Select: ");
                    String action = scanner.nextLine().trim();

                    int actionChoice;
                    try {
                        actionChoice = Integer.parseInt(action);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selection.");
                        break;
                    }

                    String result = msg.sentMessage(actionChoice);
                    System.out.println(result);

                    if (actionChoice == 1) {
                        messagesSentThisSession++;
                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID:   " + msg.getMessageID());
                        System.out.println("Message Hash: " + msg.getMessageHash());
                        System.out.println("Recipient:    " + msg.getRecipientCell());
                        System.out.println("Message:      " + msg.getMessageText());
                    }
                    break;

                case "2":
                    System.out.println(chatapp.Message.printMessages());
                    break;

                case "3":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2, or 3.");
            }
        }

        System.out.println("\n" + chatapp.Message.printMessages());
        System.out.println("Total messages sent: " + chatapp.Message.returnTotalMessages());
        System.out.println("\nGoodbye!");
        scanner.close();
    }
}
