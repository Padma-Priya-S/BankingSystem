package com.hexaware.entity;

import java.util.Date;
import java.util.List;

/**
 *this is an interface that consists several methods that show the operations that customer can do.
 */
public interface CustomerServiceProvider {
  double getAccountBalance(long accountNumber);

  double deposit(long accountNumber, double amount);

  double withdraw(long accountNumber, double amount);

  boolean transfer(long fromAccountNumber, long toAccountNumber, double amount);

  String getAccountDetails(long accountNumber);
    
  List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate);
}

