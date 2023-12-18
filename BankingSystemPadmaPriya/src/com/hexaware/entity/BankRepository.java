package com.hexaware.entity;

import com.hexaware.exceptions.InsufficientFundException;
import com.hexaware.exceptions.InvalidAccountException;
import com.hexaware.exceptions.OverDraftLimitExceededException;
import java.util.Date;
import java.util.List;

/**
 * this is an interface that consists several methods that show the operations happen in a bank.
 */
public interface BankRepository {
  void createAccount(Customer customer, long accNo, 
        String accType, float balance); 
    
  List<Account> listAccounts();
    
  double calculateInterest(); 
    
  double getAccountBalance(long accountNumber);
  
  double deposit(long accountNumber, float amount)throws InvalidAccountException;  
  
  double withdraw(long accountNumber, float amount) throws InvalidAccountException,
      OverDraftLimitExceededException, InsufficientFundException; 
  
  boolean transfer(long fromAccountNumber, long toAccountNumber, float amount);   
  
  String getAccountDetails(long accountNumber);    
  
  List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate);
}
