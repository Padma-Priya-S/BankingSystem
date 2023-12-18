package com.hexaware.services;

import com.hexaware.entity.Account;
import com.hexaware.entity.CustomerServiceProvider;
import com.hexaware.entity.Transaction;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class is to implement all the methods of the iCustomerServiceProvider interface.
 */
public class CustomerServiceProviderImpl implements CustomerServiceProvider {
  protected Map<Long, Account> accounts;
  
  public CustomerServiceProviderImpl() {
    accounts = new HashMap<>();
  }

  @Override
  public double getAccountBalance(long accountNumber) {
    Account account = accounts.get(accountNumber);
    return (account != null) ? account.getAccountBalance() : -1;
  }

  @Override
  public double deposit(long accountNumber, double amount) {
    Account account = accounts.get(accountNumber);
    if (account != null) {
      account.deposit(amount);
      return account.getAccountBalance();
    } else {
      System.out.println("Account not found.");
      return -1;
    }
  }

  @Override
  public double withdraw(long accountNumber, double amount) {
    Account account = accounts.get(accountNumber);
    if (account != null) {
      account.withdraw(amount);
      return account.getAccountBalance();
    } else {
      System.out.println("Account not found.");
      return -1;
    }
  }

  @Override
  public boolean transfer(long fromAccountNumber, long toAccountNumber, double amount) {
    Account fromAccount = accounts.get(fromAccountNumber);
    Account toAccount = accounts.get(toAccountNumber);
    if (fromAccount == null || toAccount == null) {
      System.out.println("One or both accounts not found.");
      return false;
    }
    if (fromAccount.getAccountBalance() < amount) {
      System.out.println("Insufficient balance for transfer.");
      return false;
    }
    fromAccount.withdraw(amount);
    toAccount.deposit(amount);
    System.out.println("Transfer successful.");
    return true;
  }

  @Override
  public String getAccountDetails(long accountNumber) {
    Account account = accounts.get(accountNumber);
    if (account != null) {
      return account.toString();
    } else {
      return "Account not found.";
    }
  }
    
  @Override
  public List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) {
    Account account = accounts.get(accountNumber);
    if (account != null) {
      return account.getTransactionsBetweenDates(fromDate, toDate);
    } else {
      System.out.println("Account not found.");
      return null;
    }
  }
}
