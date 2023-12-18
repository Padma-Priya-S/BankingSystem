package com.hexaware.view;

import com.hexaware.controller.Bank;
import com.hexaware.entity.Account;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Transaction;
import com.hexaware.exceptions.InsufficientFundException;
import com.hexaware.exceptions.InvalidAccountException;
import com.hexaware.exceptions.OverDraftLimitExceededException;
import com.hexaware.util.TransactionUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * this class basically deals with user interaction.
 * it imports several other classes of the project.
 */
public class BankApp {
  /**
   * this is the main method of the project.
   * it shows a menu and provides details accordingly.

   * @param args the command line arguments.
   * @throws InvalidAccountException to handle the exception
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Bank bank = new Bank();
    Map<Integer, Customer> customers = new HashMap<>();
    boolean running = true;
    while (running) {
      System.out.println("\nSelect an operation:");
      System.out.println("1. Create Account");
      System.out.println("2. Get Account Balance");
      System.out.println("3. Deposit");
      System.out.println("4. Withdraw");
      System.out.println("5. Transfer");
      System.out.println("6. Get Account Details");
      System.out.println("7. List Accounts");
      System.out.println("8. Get Transactions");
      System.out.println("9. Exit");
      System.out.print("Enter your choice: ");
      int choice = scanner.nextInt(); 
      switch (choice) {
        case 1:
          createAccount(bank, customers, scanner);
          break;
        case 2:
          getAccountBalance(bank, scanner);
          break;
        case 3:
          deposit(bank, scanner);
          break;
        case 4:
          withdraw(bank, scanner);
          break;
        case 5:
          transfer(bank, scanner);
          break;
        case 6:
          getAccountDetails(bank, customers, scanner);
          break;
        case 7:
          listAccounts(bank, customers);
          break;
        case 8:
          getTransactions(bank, scanner);
          break;
        case 9:
          running = false;
          break;
        default:
          System.err.println("Invalid choice! Please try again.");
      }
    }
  }
    
  private static void createAccount(Bank bank, Map<Integer, Customer> customers, 
      Scanner scanner) {
    System.out.println("Enter customer ID: ");
    int customerId = scanner.nextInt();
    System.out.println("Enter first name: ");
    String firstName = scanner.next();
    System.out.println("Enter last name: ");
    String lastName = scanner.next();
    System.out.println("Enter email address: ");
    String emailAddress = scanner.next();
    System.out.println("Enter phone number: ");
    String phoneNumber = scanner.next();
    System.out.println("Enter address: ");
    String address = scanner.next();  
    Customer customer = new Customer(customerId, firstName, lastName, 
        emailAddress, phoneNumber, address);
    System.out.println("Enter account type" + "\n" + "Savings" + "\n" + "Current");
    String accType = scanner.next();
    if (accType.equalsIgnoreCase("savings")) {
      System.out.println("Min balance is RS500. Enter initial balance for savings account:");
    } else {
      System.out.println("Enter initial balance for current account:");
    }
    double balance = scanner.nextDouble();
    bank.createAccount(customer, accType, (float) balance);
  }
    
  private static void getAccountBalance(Bank bank, Scanner scanner) {
    System.out.println("Enter account number: ");
    long accountNumber = scanner.nextLong(); 
    double balance = bank.getAccountBalance(accountNumber);
    if (balance != -1) {
      System.out.println("Account balance: " + balance);
    }
  }
    
  private static void deposit(Bank bank, Scanner scanner) {
    try {
      System.out.println("Enter account number: ");
      long accountNumber = scanner.nextLong();
      System.out.println("Enter deposit amount: ");
      double amount = scanner.nextDouble();
      double newBalance = bank.deposit(accountNumber, amount);
      if (newBalance != -1) {
        System.out.println("New account balance: " + newBalance);
        long transactionId = TransactionUtils.generateTransactionId();
        Transaction depositTransaction = new Transaction(accountNumber, transactionId, 
            "Deposit", (float) amount, new Date());
        bank.addTransaction(depositTransaction);
      }
    } catch (InvalidAccountException e) {
      System.err.println("Exception occurred: " + e.getMessage());
    }
  }
    
  private static void withdraw(Bank bank, Scanner scanner) {
    try {
      System.out.println("Enter account number: ");
      long accountNumber = scanner.nextLong(); 
      System.out.println("Enter withdraw amount: ");
      double amount = scanner.nextDouble();
      double newBalance = bank.withdraw(accountNumber, amount);
      if (newBalance != -1) {
        System.out.println("New account balance: " + newBalance);
        long transactionId = TransactionUtils.generateTransactionId();
        Transaction depositTransaction = new Transaction(accountNumber, transactionId, 
            "Withdraw", (float) amount, new Date());
        bank.addTransaction(depositTransaction);
      }
    } catch (InvalidAccountException | InsufficientFundException 
         | OverDraftLimitExceededException e) {
      System.err.println("Exception occurred: " + e.getMessage());
    } catch (NullPointerException e) {
      System.err.println("NullPointerException occurred: " + e.getMessage());
    }
  }


  private static void transfer(Bank bank, Scanner scanner) {
    System.out.println("Enter from account number: ");
    long fromAccount = scanner.nextLong();
    System.out.println("Enter to account number: ");
    long toAccount = scanner.nextLong();
    System.out.println("Enter transfer amount: ");
    double amount = scanner.nextDouble();
    boolean transferSuccess = bank.transfer(fromAccount, toAccount, amount);
    if (transferSuccess) {
      System.out.println("Amount transferred successfully!");
    } else {
      System.err.println("Transfer failed. Please check account details and balance.");
    }
  }
    
  private static void getAccountDetails(Bank bank, 
      Map<Integer, Customer> customers, Scanner scanner) {
    System.out.println("Enter account number: ");
    long accountNumber = scanner.nextLong();
    scanner.nextLine(); 
    String accountDetails = bank.getAccountDetails(accountNumber, customers);
    if (!accountDetails.equals("Account not found.")) {
      System.out.println("Account Details for Account Number " 
          + accountNumber + ":\n" + accountDetails);
    } else {
      System.err.println("Account not found!");
    }
  }
  
  /**
   * Retrieve and display the list of accounts.

   * @param bank which carries the attribues of bank class.
   * @param customers it is a map of customers that has accounts.
   */
  public static void listAccounts(Bank bank, Map<Integer, Customer> customers) {
    List<Account> accounts = bank.listAccounts();
    System.out.println("\nList of Accounts:");
    for (Account account : accounts) {
      int accountId = account.getAccountId();
      int customerId = account.getCustomerId();
      String accounttype = account.getAccountType();
      double balance = account.getAccountBalance();
      System.out.println("Account ID: " + accountId + ", Customer ID: " 
          + customerId + ", Account Type: " + accounttype + ", Balance: " + balance);
    }
  }
  
  /**
   * this methods lists all the transactions between given dates.

   * @param bank this Bank class object is used to call the getTransactions() in Bank class.
   * @param scanner this is used to take inputs from user like account number, fromdate, todate.
   */
  public static void getTransactions(Bank bank, Scanner scanner) {
    System.out.println("Enter account number: ");
    long accountNumber = scanner.nextLong();
    scanner.nextLine(); 
    System.out.println("Enter start date (yyyy-MM-dd): ");
    String fromDateStr = scanner.nextLine();
    System.out.println("Enter end date (yyyy-MM-dd): ");
    String toDateStr = scanner.nextLine();
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date fromDate = formatter.parse(fromDateStr);
      Date toDate = formatter.parse(toDateStr);
      Account account = bank.getAccount(accountNumber);
      if (account != null) {
        List<Transaction> transactions = bank.getTransactions(accountNumber, fromDate, toDate);
        if (!transactions.isEmpty()) {
          System.out.println("Transactions between " + fromDateStr + " and " + toDateStr + ":");
          for (Transaction transaction : transactions) {
            long transactionId = transaction.getTransactionId();
            String transactionType = transaction.getTransactionType();
            float transactionAmount = transaction.getTransactionAmount();
            Date transactionDate = transaction.getDate();
            System.out.println("transactionId : " + transactionId + ", transactionType : " 
                + transactionType + ", transactionAmount : " + transactionAmount 
                + ", transactionDate : " + transactionDate);
          }                    
        } else {
          System.err.println("No transactions found.");
        }
      } else {
        System.err.println("Account not found.");
      }
    } catch (ParseException e) {
      System.err.println("Invalid date format. Please use yyyy-MM-dd.");
    }
  }

}

