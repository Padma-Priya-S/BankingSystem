package com.hexaware.exceptions;

/**
 * this is used to handle when user wants to perform bank operations without having an account.
 */
public class InvalidAccountException extends Exception {


  /**
  * this is a defaultserialVersionUID.
  */
  private static final long serialVersionUID = -5060649561632451041L;

  public InvalidAccountException(String message) {
    super(message);
  }
}
