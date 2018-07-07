package edu.buaa.rse.dotx.zk;

public class ZKException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 7456917681092569677L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ZKException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause.
     */
    public ZKException(String message, Throwable cause) {
       super(message,cause);	
    }
}    