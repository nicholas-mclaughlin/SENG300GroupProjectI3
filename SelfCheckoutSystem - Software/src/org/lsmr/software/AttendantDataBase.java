package org.lsmr.software;


import java.util.Hashtable;

/*
    Attendant information database
    Used for verifying attendant information.
 */

public class AttendantDataBase {
    private Hashtable<String,String> attendantData;

    //constructor
    public AttendantDataBase(){
        this.attendantData = new Hashtable<>();
        this.attendantData.put("admin","admin");
    }

    /*
        Add a new entry to database

        @param
        String username
        String password

        @return
        true： adding successfully
        false: cannot add
     */
    public boolean addEntry(String username,String password){
        if(!verifyExistence(username)){
            attendantData.put(username,password);
            return true;
        }else{
            return false;
        }
    }

    /*
    Login
    @param
    String username
    String password

    @return
    true: username and passowrd match, can login
    false: username does not exist, or username and passowrd does not match
    */
    public boolean logIn(String username,String password){
        if(verifyExistence(username)){
            return password.equals(attendantData.get(username));
        }
        return false;
    }

    /*
    Remove an entry int the database, the current working account cannot be removed
    @param
    String current username: current working username of console
    String username: the username want to be removed

    @return
    true: remove entry sucessfully
    false: fail to remove the entry
     */
    public boolean removeEntry(String currentUsername, String username){
        if(verifyExistence(username) && !currentUsername.equals(username)){
            attendantData.remove(username);
            return true;
        }else{
            return false;
        }
    }

    /*
    check an account is in the database or not
    @param
    String username: a username want to be verified

    @return
    true： account is existed in the datasbase
    false: account does not exist in the database
     */
    public boolean verifyExistence(String username){
        return attendantData.containsKey(username);
    }
}
