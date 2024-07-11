import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class Bank {
    private Map<String, Double> accounts;
    private Map<String, List<Transaction>> transactionHistory;

    public Bank() {
        accounts = new HashMap<>();
        transactionHistory = new HashMap<>();

        // Initialize some sample accounts
        accounts.put("priya", 1000.0);
        accounts.put("jannu", 2000.0);
    }

    public boolean validateUser(String userId, String pin) {
        // Check if the user ID and PIN are valid
        return accounts.containsKey(userId) && pin.equals("1234"); // Replace "1234" with the actual PIN logic
    }

    public void displayTransactionHistory(String userId) {
        // Display transaction history for the user
        if (transactionHistory.containsKey(userId)) {
            List<Transaction> userTransactions = transactionHistory.get(userId);
            System.out.println("Transaction History for User ID: " + userId);
            for (Transaction transaction : userTransactions) {
                System.out.println("Type: " + transaction.getType() + ", Amount: $" + transaction.getAmount());
            }
        } else {
            System.out.println("No transaction history found for User ID: " + userId);
        }
    }

    public void withdraw(String userId, double amount) {
        // Withdraw money from the user's account
        if (accounts.containsKey(userId)) {
            double balance = accounts.get(userId);
            if (amount > 0 && amount <= balance) {
                accounts.put(userId, balance - amount);
                List<Transaction> userTransactions = transactionHistory.getOrDefault(userId, new LinkedList<>());
                userTransactions.add(new Transaction("Withdraw", amount));
                transactionHistory.put(userId, userTransactions);
                System.out.println("Withdrawn $" + amount + " successfully.");
            } else {
                System.out.println("Invalid amount or insufficient balance.");
            }
        }
    }

    public void deposit(String userId, double amount) {
        // Deposit money into the user's account
        if (accounts.containsKey(userId) && amount > 0) {
            double balance = accounts.get(userId);
            accounts.put(userId, balance + amount);
            List<Transaction> userTransactions = transactionHistory.getOrDefault(userId, new LinkedList<>());
            userTransactions.add(new Transaction("Deposit", amount));
            transactionHistory.put(userId, userTransactions);
            System.out.println("Deposited $" + amount + " successfully.");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    public void transfer(String userId, String targetUserId, double amount) {
        // Transfer money from one user's account to another
        if (accounts.containsKey(userId) && accounts.containsKey(targetUserId)) {
            double senderBalance = accounts.get(userId);
            double receiverBalance = accounts.get(targetUserId);

            if (amount > 0 && amount <= senderBalance) {
                accounts.put(userId, senderBalance - amount);
                accounts.put(targetUserId, receiverBalance + amount);

                List<Transaction> senderTransactions = transactionHistory.getOrDefault(userId, new LinkedList<>());
                senderTransactions.add(new Transaction("Transfer (to " + targetUserId + ")", amount));
                transactionHistory.put(userId, senderTransactions);

                List<Transaction> receiverTransactions = transactionHistory.getOrDefault(targetUserId, new LinkedList<>());
                receiverTransactions.add(new Transaction("Transfer (from " + userId + ")", amount));
                transactionHistory.put(targetUserId, receiverTransactions);

                System.out.println("Transferred $" + amount + " to " + targetUserId + " successfully.");
            } else {
                System.out.println("Invalid amount or insufficient balance.");
            }
        }
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the ATM system!");

        while (true) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();

            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            if (bank.validateUser(userId, pin)) {
                System.out.println("Login successful!");
                while (true) {
                    System.out.println("\nChoose an operation:");
                    System.out.println("1. Transaction History");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Deposit");
                    System.out.println("4. Transfer");
                    System.out.println("5. Quit");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    switch (choice) {
                        case 1:
                            bank.displayTransactionHistory(userId);
                            break;
                        case 2:
                            System.out.print("Enter amount to withdraw: $");
                            double withdrawAmount = scanner.nextDouble();
                            scanner.nextLine(); // Consume the newline character
                            bank.withdraw(userId, withdrawAmount);
                            break;
                        case 3:
                            System.out.print("Enter amount to deposit: $");
                            double depositAmount = scanner.nextDouble();
                            scanner.nextLine(); // Consume the newline character
                            bank.deposit(userId, depositAmount);
                            break;
                        case 4:
                            System.out.print("Enter recipient's User ID: ");
                            String targetUserId = scanner.nextLine();
                            System.out.print("Enter amount to transfer: $");
                            double transferAmount = scanner.nextDouble();
                            scanner.nextLine(); // Consume the newline character
                            bank.transfer(userId, targetUserId, transferAmount);
                            break;
                        case 5:
                            System.out.println("Thank you for using the ATM. Goodbye!");
                            return;
                        default:
                            System.out.println("Invalid choice. Please choose a valid option.");
                    }
                }
            } else {
                System.out.println("Invalid User ID or PIN. Please try again.");
            }
        }
    }
}