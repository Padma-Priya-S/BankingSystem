package com.hexaware.entity;

import java.util.Date;

/**
 * this class deals with the transactions made by the user for any account.
 */
public class Transaction {
  private long accountId;
  private long transactionId;
  private String transactionType;
  private float transactionAmount;
  private Date date;
    
  /**
   * parameterized constructor for the class.

   * @param accountId to uniquely identify any account.
   * @param transactionId to uniquely identify any transaction.
   * @param transactionType whether deposit or withdraw.
   * @param transactionAmount how much amount is involved in the transaction.
   * @param date on which date that transaction happened.
   */
  public Transaction(long accountId, long transactionId, 
      String transactionType, float transactionAmount, Date date) {
    this.accountId = accountId;
    this.transactionId = transactionId;
    this.transactionType = transactionType;
    this.transactionAmount = transactionAmount;
    this.date = date;
  }

  // Getters and setters for the attributes
  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }

  public long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(long transactionId) {
    this.transactionId = transactionId;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public float getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(float transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
