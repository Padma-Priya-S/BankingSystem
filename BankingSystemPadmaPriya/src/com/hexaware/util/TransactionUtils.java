package com.hexaware.util;

/**
 * this utility class is to generate transaction ids for each new transaction.
 */
public class TransactionUtils {
  private static long lastTransactionId = 119;

  public static synchronized long generateTransactionId() {
    lastTransactionId++;
    return lastTransactionId;
  }
}
