/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.*;
import model.User;
/**
 *
 * @author phamlegiakiet
 */
public class UserManager {
    static Map<String,User> userMap;
    public UserManager(Map<String,User> userMap ){
        this.userMap = userMap;
    }
    public void addUser(User user){
         this.userMap.put(user.getId(),user
         );
    }
    public void removeUser(String userID){
        this.userMap.remove(userID);
    }
    public User hasUser(String userID, String password){
        User user = this.userMap.get(userID);
        if(user == null){
            return null;
        }
        if(user.getPassword().equalsIgnoreCase( password)){
            return user;
        };
        return null;
    }
    public Map<String,User> getUserMap(){
        return this.userMap;
    }
}
