package com.hexaware.exceptions;

/**
 * this is an exception class.
 * this is used to handle when user wants to withdraw or transfer,
 * amount that is greater than available balance.
 */
public class InsufficientFundException extends Exception {
  
  /**
  * this is a defaultserialVersionUID.
  */
  private static final long serialVersionUID = 7020289347627132728L;

  public InsufficientFundException(String message) {
    super(message);
  }
}
