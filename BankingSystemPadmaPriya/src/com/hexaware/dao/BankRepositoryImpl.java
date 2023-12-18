package com.hexaware.dao;

import com.hexaware.entity.Account;
import com.hexaware.entity.BankRepository;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Transaction;
import com.hexaware.exceptions.InsufficientFundException;
import com.hexaware.exceptions.InvalidAccountException;
import com.hexaware.exceptions.OverDraftLimitExceededException;
import com.hexaware.util.DbUtil;
//import com.hexaware.util.TransactionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * this class is basically a dao class that interacts with the HMBank database.
 * to retreive corresponding data.
 */
public class BankRepositoryImpl implements BankRepository {

  private Connection connection;

  @Override
  public void createAccount(Customer customer, long accNo, String accType, float balance) {
    try {
      connection = DbUtil.getDbConn();
      String inserttQuery = "INSERT INTO customers (customer_id, first_Name, "
          + "last_Name, email, address) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement preparedstamentt = connection.prepareStatement(inserttQuery);
      preparedstamentt.setLong(1, customer.getCustomerId());
      preparedstamentt.setString(2, customer.getFirstName());
      preparedstamentt.setString(3, customer.getLastName());
      preparedstamentt.setString(4, customer.getEmailAddress());
      preparedstamentt.setString(5, customer.getAddress());
      preparedstamentt.executeUpdate();
      String insertQuery = "INSERT INTO accounts (account_id, customer_id, "
          + "acount_type, balance) VALUES (?, ?, ?, ?)";
      PreparedStatement preparedstament = connection.prepareStatement(insertQuery);
      preparedstament.setLong(1, accNo);
      preparedstament.setInt(2, customer.getCustomerId());
      preparedstament.setString(3, accType);
      preparedstament.setFloat(4, balance);
      preparedstament.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Account> listAccounts() {
    List<Account> accountList = new ArrayList<>();
    try {
      connection = DbUtil.getDbConn();
      String selectQuery = "SELECT * FROM accounts";
      PreparedStatement preparedstatement = connection.prepareStatement(selectQuery);
      ResultSet resultset = preparedstatement.executeQuery();
      while (resultset.next()) {
        Account account = new Account(
            resultset.getInt("account_id"),
            resultset.getInt("customer_id"),
            resultset.getString("account_id"),
            resultset.getString("acount_type"),
            resultset.getFloat("balance")
        );
        accountList.add(account);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return accountList;
  }

  @Override
  public double calculateInterest() {
    double totalInterest = 0.0;
    try {
      connection = DbUtil.getDbConn();
      String selectQuery = "SELECT SUM(balance * interest_rate / 100) "
          + "AS total_interest FROM accounts";
      PreparedStatement preparedstament = connection.prepareStatement(selectQuery);
      ResultSet resultset = preparedstament.executeQuery();
      if (resultset.next()) {
        totalInterest = resultset.getDouble("total_interest");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return totalInterest;
  }

  @Override
  public double getAccountBalance(long accountNumber) {
    double balance = 0.0;
    try {
      connection = DbUtil.getDbConn();
      String selectQuery = "SELECT balance FROM accounts WHERE account_id = ?";
      PreparedStatement preparedstament = connection.prepareStatement(selectQuery);
      preparedstament.setLong(1, accountNumber);
      ResultSet resultset = preparedstament.executeQuery();
      if (resultset.next()) {
        balance = resultset.getDouble("balance");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return balance;
  }
  
  @Override
  public double deposit(long accountNumber, float amount) throws InvalidAccountException {
    double newBalance = -1;
    try {
      connection = DbUtil.getDbConn();
      connection.setAutoCommit(false);
      String selectQuery = "SELECT balance FROM accounts WHERE account_id = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
      preparedStatement.setLong(1, accountNumber);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        double currentBalance = resultSet.getDouble("balance");
        double updatedBalance = currentBalance + amount;
        String updateQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setDouble(1, updatedBalance);
        updateStatement.setLong(2, accountNumber);
        int rowsUpdated = updateStatement.executeUpdate();
        if (rowsUpdated > 0) {
          newBalance = updatedBalance;
          connection.commit();
        } else {
          connection.rollback();
        }
      } else {
        connection.rollback();
        throw new InvalidAccountException("Invalid account number");
      }
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return newBalance;
  }
  
  @Override
  public double withdraw(long accountNumber, float amount) throws InsufficientFundException,
      OverDraftLimitExceededException, InvalidAccountException {
    double newBalance = -1;
    try {
      connection = DbUtil.getDbConn();
      connection.setAutoCommit(false);
      String selectQuery = "SELECT balance, acount_type FROM accounts WHERE account_id = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
      preparedStatement.setLong(1, accountNumber);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        double currentBalance = resultSet.getDouble("balance");
        String accountType = resultSet.getString("acount_type");
        if (accountType.equalsIgnoreCase("savings") || accountType.equalsIgnoreCase("current")) {
          if (currentBalance >= amount) {
            double updatedBalance = currentBalance - amount;
            String updateQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, updatedBalance);
            updateStatement.setLong(2, accountNumber);
            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated > 0) {
              newBalance = updatedBalance;
              connection.commit();
            } else {
              connection.rollback();
            }
          } else {
            connection.rollback();
            throw new InsufficientFundException("Insufficient funds in the account");
          }
        } else {
          connection.rollback();
          throw new OverDraftLimitExceededException("Account type not supported for withdrawal");
        }
      } else {
        connection.rollback();
        throw new InvalidAccountException("Invalid account number");
      }
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return newBalance;
  }


  @Override
  public boolean transfer(long fromAccountNumber, long toAccountNumber, float amount) {
    boolean transferSuccess = false;
    try {
      connection = DbUtil.getDbConn();
      connection.setAutoCommit(false);
      String selectFromAccountQuery = "SELECT balance FROM accounts WHERE account_id = ?";
      PreparedStatement preparedstament = connection.prepareStatement(
          selectFromAccountQuery);
      preparedstament.setLong(1, fromAccountNumber);
      ResultSet resultset = preparedstament.executeQuery();
      String selectToAccountQuery = "SELECT balance FROM accounts WHERE account_id = ?";
      PreparedStatement preparedstamentt = connection.prepareStatement(selectToAccountQuery);
      preparedstamentt.setLong(1, toAccountNumber);
      ResultSet toAccountRs = preparedstamentt.executeQuery();
      if (resultset.next() && toAccountRs.next()) {
        double fromAccountBalance = resultset.getDouble("balance");
        double toAccountBalance = toAccountRs.getDouble("balance");
        if (fromAccountBalance >= amount) {
          double newFromAccountBalance = fromAccountBalance - amount;
          double newToAccountBalance = toAccountBalance + amount;
          String updateFromAccountQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
          PreparedStatement preparedstatement = connection.prepareStatement(
              updateFromAccountQuery);
          preparedstatement.setDouble(1, newFromAccountBalance);
          preparedstatement.setLong(2, fromAccountNumber);
          int fromAccountRowsUpdated = preparedstatement.executeUpdate();
          String updateToAccountQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
          PreparedStatement preparedstatement1 = connection.prepareStatement(
              updateToAccountQuery);
          preparedstatement1.setDouble(1, newToAccountBalance);
          preparedstatement1.setLong(2, toAccountNumber);
          int toAccountRowsUpdated = preparedstatement1.executeUpdate();
          if (fromAccountRowsUpdated > 0 && toAccountRowsUpdated > 0) {
            transferSuccess = true;
            connection.commit();
          } else {
            connection.rollback();
          }
        } else {
          connection.rollback();
        }
      } else {
        connection.rollback();
      }
      connection.setAutoCommit(true);
      resultset.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transferSuccess;
  }

  @Override
  public String getAccountDetails(long accountNumber) {
    StringBuilder details = new StringBuilder();
    try {
      connection = DbUtil.getDbConn();
      String selectAccountQuery = "SELECT * FROM accounts WHERE account_id = ?";
      PreparedStatement preparedstatement = connection.prepareStatement(
          selectAccountQuery);
      preparedstatement.setLong(1, accountNumber);
      ResultSet accountRs = preparedstatement.executeQuery();
      if (accountRs.next()) {
        int accountId = accountRs.getInt("account_id");
        int customerId = accountRs.getInt("customer_id");
        String accountType = accountRs.getString("acount_type");
        double accountBalance = accountRs.getDouble("balance");
        details.append("Account ID: ").append(accountId).append("\n");
        details.append("Customer ID: ").append(customerId).append("\n");
        details.append("Account Number: ").append(accountNumber).append("\n");
        details.append("Account Type: ").append(accountType).append("\n");
        details.append("Account Balance: ").append(accountBalance).append("\n");
        // Fetch customer details if available
        String selectCustomerQuery = "SELECT * FROM customers WHERE customer_id = ?";
        PreparedStatement preparedstatement1 = connection.prepareStatement(selectCustomerQuery);
        preparedstatement1.setInt(1, customerId);
        ResultSet resultset = preparedstatement1.executeQuery();
        if (resultset.next()) {
          String firstName = resultset.getString("first_name");
          String lastName = resultset.getString("last_name");
          details.append("\nCustomer Details:\n");
          details.append("First Name: ").append(firstName).append("\n");
          details.append("Last Name: ").append(lastName).append("\n");
          String emailAddress = resultset.getString("email");
          details.append("Email Address: ").append(emailAddress).append("\n");
          String phoneNumber = resultset.getString("phone_number");
          details.append("Phone Number: ").append(phoneNumber).append("\n");
          String address = resultset.getString("address");
          details.append("Address: ").append(address).append("\n");
          resultset.close();
        }
      } else {
        details.append("Account not found.");
      }
      preparedstatement.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return details.toString();
  }

  @Override
  public List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) {
    List<Transaction> transactions = new ArrayList<>();
    try {
      connection = DbUtil.getDbConn();
      String selectTransactionsQuery = "SELECT * FROM transactions WHERE account_id = ? AND  "
          + "transaction_date BETWEEN ? AND ?";
      PreparedStatement preparedstatement = connection.prepareStatement(
          selectTransactionsQuery);
      preparedstatement.setLong(1, accountNumber);
      preparedstatement.setDate(2, new java.sql.Date(fromDate.getTime()));
      preparedstatement.setDate(3, new java.sql.Date(toDate.getTime()));
      ResultSet resultset = preparedstatement.executeQuery();
      while (resultset.next()) {
        long transactionId = resultset.getLong("transaction_id");
        String transactionType = resultset.getString("transaction_type");
        float transactionAmount = resultset.getFloat("amount");
        Date transactionDate = resultset.getDate("transaction_date");
        Transaction transaction = new Transaction(transactionId, 
            accountNumber, transactionType, transactionAmount, transactionDate);
        transactions.add(transaction);
      }
      //closing resources
      resultset.close();
      preparedstatement.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transactions;
  }
  
  /**
   * this method takes account number as input.
   * and it retreives all the accounts with that particular account id.

   * @param accountNumber the account number of a customer's account.
   * @return Account it returns the account object.
   */
  public Account getAccount(long accountNumber) {
    Account account = null;
    try {
      // Establish database connection and execute SQL query
      Connection connection = DbUtil.getDbConn();
      String selectQuery = "SELECT * FROM accounts WHERE account_id = ?";
      PreparedStatement preparedstatement = connection.prepareStatement(selectQuery);
      preparedstatement.setLong(1, accountNumber);
      ResultSet rs = preparedstatement.executeQuery();
      // Check if the account exists and retrieve its details
      if (rs.next()) {
        account = new Account(
        rs.getInt("account_id"),
        rs.getInt("customer_id"),
        rs.getString("account_id"),
        rs.getString("acount_type"),
        rs.getFloat("balance")
      );
      }        
      // Close resources
      rs.close();
      preparedstatement.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return account;
  }
  
  /**
   * this method is to add transactions whenever a deposit or withdraw happens.

   * @param transaction this object consists required attributes to perform the operation.
   * @return returns a boolean value stating whether added successfully or not.
   */
  public boolean addTransaction(Transaction transaction) {
    boolean isSuccess = false;
    try {
      connection = DbUtil.getDbConn();
      connection.setAutoCommit(false);
      System.out.println("Hello1");
      String insertQuery = "INSERT INTO transactions (account_id, transaction_id,"
          + " transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);          
      preparedStatement.setLong(1, transaction.getAccountId());
      preparedStatement.setInt(2, (int) transaction.getTransactionId());
      preparedStatement.setString(3, transaction.getTransactionType());
      preparedStatement.setFloat(4, transaction.getTransactionAmount());
      preparedStatement.setDate(5, new java.sql.Date(transaction.getDate().getTime()));
      System.out.println("Hello2");
      int rowsInserted = preparedStatement.executeUpdate();
      System.out.println("Hello3");
      if (rowsInserted > 0) {
        System.out.println("Hello4"); 
        connection.commit();
        System.out.println("Transaction inserted successfully.");
        isSuccess = true;
      } else {
        connection.rollback();
        System.err.println("Failed to insert transaction.");
      }
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isSuccess;
  }
}

