package com.hexaware.exceptions;

/**
 * this is used to handle when user wants.
 * to withdraw amount greater than OverDraftLimit.
 * for the current account type.
 */
public class OverDraftLimitExceededException extends Exception {
  /**
  * this is a defaultserialVersionU.
  */
  private static final long serialVersionUID = -5424383517529728264L;
  
  public OverDraftLimitExceededException(String message) {
    super(message);
  }
}
