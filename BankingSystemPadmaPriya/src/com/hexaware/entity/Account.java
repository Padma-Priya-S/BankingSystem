package com.hexaware.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class basically deals with the details of the account.
 * for a particular customer.
 */
public class Account {
  protected int accountId; 
  protected int customerId;
  private int transactionIdCounter = 11000;
  protected String accountNumber;
  protected String accountType;
  protected double accountBalance;
  private Customer customer; 
  private static List<Transaction> transactions;

  /**
   * This is a default constructor.
   * that assigns default values to the class attributes.
   */
  public Account() {
    this.accountNumber = "";
    this.accountType = "";
    this.accountBalance = 0.0;
    transactions = new ArrayList<>();
  }


  /**
   * this is a parameterized constructor.

   * @param accountId to uniquely identify any account.
   * @param customerId to connect customer and account.
   * @param accountNumber the account Number.
   * @param accountType whether savings or current account.
   * @param accountBalance the balance of the particular account.
   */
  public Account(int accountId, int customerId, String accountNumber, 
      String accountType, double accountBalance) {
    this.accountId = accountId;
    this.customerId = customerId;
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.accountBalance = accountBalance;
    transactions = new ArrayList<>();
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Customer getCustomer() {
    return customer;
  }
    
  // Getter and setter methods
  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public double getAccountBalance() {
    return accountBalance;
  }

  public void setAccountBalance(double accountBalance) {
    this.accountBalance = accountBalance;
  }

  /**
   * this method prints all the attributes of account class.
   */
  public void printInfo() {
    System.out.println("Account ID: " + accountId);
    System.out.println("Customer ID: " + customerId);
    System.out.println("Account Number: " + accountNumber);
    System.out.println("Account Type: " + accountType);
    System.out.println("Account Balance: " + accountBalance);
  }

  /**
   * This method adds the amount to balance that is available already.

   * @param amount specifies how much money should be added to balance.
   */
  public void deposit(double amount) {
    accountBalance += amount;
    System.out.println("Deposited: " + amount);
    long transactionId = ++transactionIdCounter;
    if (amount > 0) {
      Transaction depositTransaction = new Transaction(accountId, transactionId, "DEPOSIT", 
          (float) amount, new Date());
      addTransaction(depositTransaction);
    }
  }
    
  public void deposit(float amount) {
    deposit((double) amount);
  }

  public void deposit(int amount) {
    deposit((double) amount);
  }

  /**
   * this method removes the specified amount.
   * from the accountbalance.

   * @param amount this gives how much should be deducted.
   */
  public void withdraw(double amount) {
    if (amount <= accountBalance) {
      accountBalance -= amount;
      long transactionId = ++transactionIdCounter;
      System.out.println("Withdrawn: " + amount);
      if (amount > 0) {
        Transaction withdrawalTransaction = new Transaction(accountId, transactionId, "WITHDRAWAL",
            (float) amount, new Date());
        addTransaction(withdrawalTransaction);
      }
    } else {
      System.err.println("Insufficient balance for withdrawal.");
    }
  }
    

  // Overloaded withdraw methods
  public void withdraw(float amount) {
    withdraw((double) amount); 
  }

  public void withdraw(int amount) {
    withdraw((double) amount); 
  }

  /**
   * this method calculates interest on account balance.
   * it has a constant interest rate of 4.5%.

   * @return it returns the calculated interest as a Double value.
   */
  public double calculateInterest() {
    // Assuming fixed interest rate of 4.5%
    double interestRate = 4.5;
    return (accountBalance * interestRate) / 100;
  }
      
  public static void addTransaction(Transaction transaction) {
    transactions.add(transaction);
  }
    
  public List<Transaction> getTransactions() {
    return transactions;
  }
    
  /**
   * method to get transactions between the specified dates.

   * @param fromDate starting date of required account's trasaction.
   * @param toDate ending date of required account's trasaction.
   * @return list of transactions that happened between those dates
   */
  public List<Transaction> getTransactionsBetweenDates(Date fromDate, Date toDate) {
    List<Transaction> transactionsInRange = new ArrayList<>();
    for (Transaction transaction : transactions) {
      Date transactionDate = transaction.getDate();
      if (transactionDate.after(fromDate) && transactionDate.before(toDate)) {
        transactionsInRange.add(transaction);
      }
    }
    return transactionsInRange;
  }




  /**
   * this subclass inherits the account class.
   * it specifies an account type.
   * the minimum balance should be RS500 while creating it.
   */
  public static class SavingsAccount extends Account {
    private double interestRate;
    private static final double MINIMUM_BALANCE = 500.0;
    
    /**
     * this is a parameterized constructor.

     * @param accountId to uniquely identify any account.
     * @param customerId to connect customer and account.
     * @param accountNumber accountNumber the account Number.
     * @param accountType accountType whether savings or current account.
     * @param accountBalance accountBalance the balance of the particular account.
     * @param interestRate specifies what is the interest rate for savings account.
     */
    public SavingsAccount(int accountId, int customerId,
        String accountNumber, String accountType, double accountBalance, double interestRate) {
      super(accountId, customerId, accountNumber, accountType, 
          (accountBalance >= MINIMUM_BALANCE ? accountBalance : 0.0));
      this.interestRate = interestRate;
      if (accountBalance >= MINIMUM_BALANCE) {
        this.interestRate = interestRate;
      } else {
        System.err.println("Initial balance must be at least " 
            + MINIMUM_BALANCE + " to open a savings account.");
      }
    }

    @Override
    public double calculateInterest() {
      return (getAccountBalance() * interestRate) / 100;
    }
  }


  /**
   * this subclass inherits the account class.
   * it specifies an account type.
   */
  public static class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 1000.0; // Constant for overdraft limit

    public CurrentAccount(int accountId, int customerId, 
        String accountNumber, String accountType, double accountBalance) {
      super(accountId, customerId, accountNumber, accountType, accountBalance);
    }

    @Override
    public void withdraw(double amount) {
      if (amount <= (getAccountBalance() + OVERDRAFT_LIMIT)) {
        setAccountBalance(getAccountBalance() - amount);
        System.out.println("Withdrawn: " + amount);
      } else {
        System.err.println("Insufficient balance for withdrawal beyond overdraft limit.");
      }
    }
  }
 
  /**
   * this is sub class that inherits the account class.
   * it specifies an acccount type.
   * balance should be zero while creating it.
   */
  public static class ZeroBalanceAccount extends Account {
    public ZeroBalanceAccount(int accountId, int customerId, 
        String accountNumber, String accountType) {
      super(accountId, customerId, accountNumber, accountType, 0.0);
    }
  }
}


