package com.puma.book.epub.exception;

import org.springframework.security.core.AuthenticationException;


public class BookNotExistException extends AuthenticationException {
   //~ Constructors ===================================================================================================

   /**
	 * 
	 */
	private static final long serialVersionUID = -7247276918090908696L;

/**
    * Constructs a <code>AccountInactiveException</code> with the specified
    * message.
    *
    * @param msg the detail message.
    */
   public BookNotExistException(String msg) {
       super(msg);
   }

   /**
    * Constructs a <code>AccountInactiveException</code>, making use of the <tt>extraInformation</tt>
    * property of the superclass.
    *
    * @param msg the detail message
    * @param extraInformation additional information such as the username.
    */
   public BookNotExistException(String msg, Object extraInformation) {
       super(msg, extraInformation);
   }

   /**
    * Constructs a <code>AccountInactiveException</code> with the specified
    * message and root cause.
    *
    * @param msg the detail message.
    * @param t root cause
    */
   public BookNotExistException(String msg, Throwable t) {
       super(msg, t);
   }
}

