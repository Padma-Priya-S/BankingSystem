package com.hexaware.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class typically deals with the personal information of each customer.
 */
public class Customer {

  // Attributes
  private int customerId;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
  private String address;
  
  // Default Constructor
  Customer() {
  }

  /**
   * This is an parameterized constructor of customer class.

   * @param customerId to uniquely identify customer as Integer.
   * @param firstName customer's first name as String.
   * @param lastName customer's last name as String.
   * @param emailAddress customer's email id as String.
   * @param phoneNumber customer's phoneNumber as String.
   * @param address customer's address as String.
   */
  public Customer(int customerId, String firstName, String lastName, String emailAddress, 
      String phoneNumber, String address) {
    this.customerId = customerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  // Getters and Setters for attributes
  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }
    
  /**
   * Checking if the email id is valid by using regex.

   * @return boolean value.
   */
  public boolean isValidEmail() {
    String emailRegex = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(emailAddress);
    return emailAddress != null && matcher.matches();
  }
  
  /**
   * this method checks if the the given email is correct.

   * @param emailAddress the customer's emailid.
   */
  public void setEmailAddress(String emailAddress) {
    if (isValidEmail()) {
      this.emailAddress = emailAddress;
    } else {
      System.err.println("Invalid email address format. Please provide a valid email.");
    }
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
    
  // checking if the give phone_number is valid
  public boolean isValidPhoneNumber() {
    return phoneNumber != null && phoneNumber.matches("\\d{10}");
    // Check if the phone number contains only digits and has a length of 10
  }
  
  /**
   * this method checks if the the given phone number is correct.
   * if it is correct, it assigns.
   *
   * @param phoneNumber contact information of customer.
   */
  public void setPhoneNumber(String phoneNumber) {
    if (isValidPhoneNumber()) {
      this.phoneNumber = phoneNumber;
    } else {
      System.err.println("Invalid phone number. Please provide a 10-digit phone number.");
    }
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * This method is to print all the attributes of customer class.
   */
  public void printCustomerInformation() {
    System.out.println("Customer ID: " + customerId);
    System.out.println("First Name: " + firstName);
    System.out.println("Last Name: " + lastName);
    System.out.println("Email Address: " + emailAddress);
    System.out.println("Phone Number: " + phoneNumber);
    System.out.println("Address: " + address);
  }
}
