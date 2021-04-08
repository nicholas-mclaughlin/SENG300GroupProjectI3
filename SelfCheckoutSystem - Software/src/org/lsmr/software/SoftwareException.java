package org.lsmr.software;

/*
* Exceptions for doing something which are not applicate to software.
* Like deliver changes which cannot be delivered based on the storage of dispenser
*
 */
public class SoftwareException extends RuntimeException {
    private String msg;
    public SoftwareException(String msg){
        this.msg = msg;
        System.out.println(msg);
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
