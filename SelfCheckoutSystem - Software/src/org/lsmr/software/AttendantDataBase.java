package org.lsmr.software;


import java.util.Hashtable;


public class AttendantDataBase {
    private Hashtable<String,String> attendantData;

    public AttendantDataBase(){
        this.attendantData = new Hashtable<>();
        this.attendantData.put("admin","admin");
    }

    public boolean addEntry(String username,String password){
        if(!verifyExistence(username)){
            attendantData.put(username,password);
            return true;
        }else{
            return false;
        }
    }

    public boolean logIn(String username,String password){
        if(verifyExistence(username)){
            return password.equals(attendantData.get(username));
        }
        return false;
    }

    public void removeEntry(String username){
        if(verifyExistence(username)){
            attendantData.remove(username);
        }
    }

    public boolean verifyExistence(String username){
        return attendantData.containsKey(username);
    }
}
