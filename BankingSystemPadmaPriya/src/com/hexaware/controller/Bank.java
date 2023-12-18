package com.hexaware.controller;

import com.hexaware.dao.BankRepositoryImpl;
import com.hexaware.entity.Account;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Transaction;
import com.hexaware.exceptions.InsufficientFundException;
import com.hexaware.exceptions.InvalidAccountException;
import com.hexaware.exceptions.OverDraftLimitExceededException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * this class is basically to operate between the database and user.
 */
public class Bank {
  private long accountNumberCounter;
  
  public Bank() {
    accountNumberCounter = 1003;
  }
    
  BankRepositoryImpl dao = new BankRepositoryImpl();
  
  /**
   * this method is to create account for the specified customer.

   * @param customer this is object of Customer class.
   * @param accType this is used to create an account of the specified type.
   * @param balance this is used to create an account with specified initial balance.
   */
  public void createAccount(Customer customer, String accType, float balance) {
    if (!customer.isValidPhoneNumber()) {
      System.err.println("Invalid phone number. Please enter a 10-digit phone number.");
      return; 
    }
    if (!customer.isValidEmail()) {
      System.err.println("Invalid email address. Please enter a valid email.");
      return;     
    }
    if (accType.equalsIgnoreCase("savings")) {
      if (balance < 500) {
        System.err.println("Minimum balance for a savings account is 500. "
            + "Account creation failed.");
        return;
      }
    }
    long accNo = accountNumberCounter;
    dao.createAccount(customer, accNo, accType, balance);
    System.out.println("Account created with number: " + accNo);
    ++accNo;
  }
    
  // Retrieve the balance of an account given its account number
  public double getAccountBalance(long accountNumber) {
    return dao.getAccountBalance(accountNumber);
  }

  // Deposit the specified amount into the account
  public double deposit(long accountNumber, double amount) throws InvalidAccountException {
    return dao.deposit(accountNumber, (float) amount);
  }

  // Withdraw the specified amount from the account
  public double withdraw(long accountNumber, double amount) throws 
      InsufficientFundException, InvalidAccountException, OverDraftLimitExceededException {
    return dao.withdraw(accountNumber, (float) amount);
  }
    
  // Transfer the specified amount
  public boolean transfer(long fromAccount, long toAccount, double amount) {
    return dao.transfer(fromAccount, toAccount, (float) amount);
  }
    
  // List all the accounts
  public List<Account> listAccounts() {
    return dao.listAccounts();
  }
    
  // list all the transactions made from the specified account
  public List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) {
    return dao.getTransactions(accountNumber, fromDate, toDate);
  }
    
  // return account object based on specified account number
  public Account getAccount(long accountNumber) {
    return dao.getAccount(accountNumber);
  }
    
  public String getAccountDetails(long accountNumber, Map<Integer, Customer> customers) {
    return dao.getAccountDetails(accountNumber); 
    // Return account details based on given account number
  }

  public boolean addTransaction(Transaction transaction) {
    return dao.addTransaction(transaction);
  }
}