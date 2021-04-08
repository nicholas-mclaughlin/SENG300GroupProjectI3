package org.lsmr.software;

/*
* Exceptions for doing something which are not applicate to software.
* Like deliver changes which cannot be delivered based on the storage of dispenser
*
 */
public class SoftwareException extends RuntimeException {
    public SoftwareException(String message){
        System.out.println(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
