package com.hexaware.entity;

/**
 * this is an interface that consists several methods that show the operations happen in a bank.
 */
public interface BankServiceProvider {
  void createAccount(Customer customer, long accNo, String accType, float balance);

  Account[] listAccounts();

  double calculateInterest();
    
  String getAccountDetails(long accountNumber);
}
