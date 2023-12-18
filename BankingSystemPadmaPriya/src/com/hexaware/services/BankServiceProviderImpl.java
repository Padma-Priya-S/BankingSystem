package com.hexaware.services;

import com.hexaware.entity.Account;
import com.hexaware.entity.BankServiceProvider;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Transaction;
import java.util.ArrayList;
import java.util.List;

/**
 * this class implements all the methods of the iBankServiceProvider interface.
 */
public class BankServiceProviderImpl extends CustomerServiceProviderImpl 
    implements BankServiceProvider {
  private List<Account> accountList;
  private List<Transaction> transactionList;
  private String branchName;
  private String branchAddress;
  
  /**
   * This is a default constructor.
   * that assigns default values to the class attributes.
   */
  public BankServiceProviderImpl() {
    super();
    accountList = new ArrayList<>();
    setTransactionList(new ArrayList<>());
  }

  /**
   * this is a parameterized constructor.

   * @param branchName to set the name of the branch as specified.
   * @param branchAddress to set the address of the branch as specified.
   */
  public BankServiceProviderImpl(String branchName, String branchAddress) {
    this();
    this.setBranchName(branchName);
    this.setBranchAddress(branchAddress);
  }

  @Override
  public void createAccount(Customer customer, long accNo, String accType, float balance) {
    Account account;
    if (accType.equalsIgnoreCase("Savings")) {
      account = new Account.SavingsAccount(generateAccountId(), customer.getCustomerId(), 
          String.valueOf(accNo), 
          accType, balance, 4.5);
    } else if (accType.equalsIgnoreCase("Current")) {
      account = new Account.CurrentAccount(generateAccountId(), 
            customer.getCustomerId(),
            String.valueOf(accNo), accType, balance);
    } else {
      account = new Account.ZeroBalanceAccount(generateAccountId(), 
            customer.getCustomerId(), 
            String.valueOf(accNo), accType);
    }
    account.setCustomer(customer);
    accountList.add(account);
  }

  private int generateAccountId() {
    // Generate an account ID based on existing accounts
    return accountList.size() + 1;
  }

  @Override
  public Account[] listAccounts() {
    return accountList.toArray(new Account[0]);
  }

  @Override
  public double calculateInterest() {
    double totalInterest = 0;
    for (Account account : accountList) {
      totalInterest += account.calculateInterest();
    }
    return totalInterest;
  }

  @Override
  public String getAccountDetails(long accountNumber) {
    for (Account account : accountList) {
      if (account.getAccountNumber().equals(String.valueOf(accountNumber))) {
        return account.toString();
      }
    }
    return "Account not found.";
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  public void setTransactionList(List<Transaction> transactionList) {
    this.transactionList = transactionList;
  }

  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public String getBranchAddress() {
    return branchAddress;
  }

  public void setBranchAddress(String branchAddress) {
    this.branchAddress = branchAddress;
  }
}
