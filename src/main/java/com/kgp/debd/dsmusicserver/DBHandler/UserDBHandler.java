/*
 * Copyright (C) 2017 Debasish Nandi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kgp.debd.dsmusicserver.DBHandler;

import com.kgp.debd.dsmusicserver.exception.AuthenticationFailedException;
import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import com.kgp.debd.dsmusicserver.exception.SignupException;
import com.kgp.debd.dsmusicserver.exception.UserNotFoundException;
import com.kgp.debd.dsmusicserver.exception.UsernameAlreadyExistsException;
import com.kgp.debd.dsmusicserver.model.User;
import com.kgp.debd.dsmusicserver.security.PBKDF2;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class UserDBHandler {
    
    private Connection connection = null;
    
    private final String USER_CRED_TABLE = "USER_CRED";
    private final String USER_DETAILS_TABLE = "DSMUSIC_USER";
    private final String USER_TRACK_TABLE = "TRACK";
    
    private final String query_insert_cred = "INSERT INTO "+ USER_CRED_TABLE +
            " (USERNAME, PASSWORD, SALT) VALUES(?, ?, ?)";
    
    private final String query_get_cred = "SELECT * FROM "+ USER_CRED_TABLE + " WHERE USERNAME = ?";
    
    private final String query_insert_details = "INSERT INTO "+ USER_DETAILS_TABLE +
            " (USERNAME, F_NAME, L_NAME, EMAIL, PHN) VALUES(?, ?, ?, ?, ?)";
    
    private final String query_insert_track = "INSERT INTO "+USER_TRACK_TABLE+
            " (USERNAME, AUDIO_COUNT, PLAYLIST_COUNT) VALUES(?, ?, ?)";
    
    private final String query_user_exists = "SELECT * FROM "+USER_CRED_TABLE+" WHERE USERNAME = ?";
    
    private final String query_user_details = "SELECT * FROM "+USER_DETAILS_TABLE+" WHERE USERNAME = ?";
    
    private final String query_get_name = "SELECT F_NAME, L_NAME FROM "+ USER_DETAILS_TABLE+" WHERE USERNAME = ?";
    private PreparedStatement pstmt = null;
    
    private ResultSet rs = null;
    
    public User getUserDetails(String username)  {
        User user = null;
        try {            
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_user_details);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                String fname = rs.getString("F_NAME");
                String lname = rs.getString("L_NAME");
                String email = rs.getString("EMAIL");
                long phn = rs.getLong("PHN");
                user = new User(phn, null, null, fname, lname, email);
            }
                
        } catch (SQLException ex) {
            throw new UserNotFoundException(ex.getMessage());
        } catch (IOException ex) {
            throw new UserNotFoundException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new UserNotFoundException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new UserNotFoundException(ex.getMessage());
        }  
        return user;
    }
    
    public String getName(String username) throws RuntimeException{
        String name = null;
        try {
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_get_name);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                String fname = rs.getString("F_NAME");
                String lname = rs.getString("L_NAME");
                name = fname+" "+lname;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Server error");
        } catch (IOException ex) {
            throw new RuntimeException("Server error");
        } catch (InvalidConfigurationException ex) {
            throw new RuntimeException("Server error");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Server error");
        }finally {
            pstmt = null;
        }
        return name;
    }
    
    public boolean isValid (String uname) throws SignupException{
        boolean isValid = false;
        
        try {
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_user_exists);
            pstmt.setString(1, uname);
            rs = pstmt.executeQuery();
            if(!rs.next())
                isValid = true;
        } catch (SQLException ex) {
            throw new SignupException(ex.getMessage());
        } catch (IOException ex) {
            throw new SignupException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new SignupException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new SignupException(ex.getMessage());
        }      
        
        return isValid;
    }
    
    public boolean insert(User user) throws SignupException, UsernameAlreadyExistsException{
        
        boolean success = false;
        String uname = user.getUsername();
        try{
            boolean flag = isValid(uname);
            if(!flag)
                throw new UsernameAlreadyExistsException("Username : "+uname+" already exists");
        }catch(NullPointerException e){
            e.printStackTrace();
            throw new SignupException(e.getMessage());
        }
            
            
        
        try {
            String saltedHash = PBKDF2.createHash(user.getPassword());
            String salt = saltedHash.split(":")[0];
            String hash = saltedHash.split(":")[1];
            
            connection = ConnectDB.connect();
            connection.setAutoCommit(false);
            
            pstmt = connection.prepareStatement(query_insert_cred);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hash);
            pstmt.setString(3, salt);
            
            int in1 = pstmt.executeUpdate();
            
            if(in1 > 0){
                
                pstmt = connection.prepareStatement(query_insert_details);
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getfName());
                pstmt.setString(3, user.getlName());
                pstmt.setString(4, user.getEmail());
                pstmt.setLong(5, user.getPhoneNumber());
                
                int in2 = pstmt.executeUpdate();
                
                if(in2 > 0){
                                                           
                    pstmt = connection.prepareStatement(query_insert_track);
                    pstmt.setString(1, user.getUsername());
                    pstmt.setInt(2, 0);
                    pstmt.setInt(3, 0);
                    
                    int in3 = pstmt.executeUpdate();
                    
                    if(in3 > 0) {
                        connection.commit();
                        success = true;
                    }else {
                        connection.rollback();
                        throw new SQLException("Error while inserting user tracking details");
                    }
                    
                    
                }else {
                    connection.rollback();
                    throw new SQLException("Error while inserting user details");
                }
            }else {
                connection.rollback();
                throw new SQLException("Error While inserting Credentials.");
            }
             
        } catch (NoSuchAlgorithmException ex) {
            throw new SignupException(ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            throw new SignupException(ex.getMessage());
        } catch (SQLException ex) {
            throw new SignupException(ex.getMessage());
        } catch (IOException ex) {
            throw new SignupException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new SignupException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new SignupException(ex.getMessage());
        }finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    System.out.println("Failed to close PreparedStatement Object : "+ex.getMessage());
                }
            }
        }
        
        
        return success;
    }
    
    public String getCredential(String username) throws AuthenticationFailedException{        
        
        String cred = null;
        String salt = null;
        String hash = null;
        
        try {    
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_get_cred);
            pstmt.setString(1, username);
            
            rs = pstmt.executeQuery();
            if(rs.next()){
                salt = rs.getString("SALT");
                hash = rs.getString("PASSWORD");
                
                // format salt:hash
                cred = salt+":"+hash;
            }
            
            
            
        } catch (SQLException ex) {
            throw new AuthenticationFailedException(ex.getMessage());
        } catch (IOException ex) {
            throw new AuthenticationFailedException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new AuthenticationFailedException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new AuthenticationFailedException(ex.getMessage());
        }
        return cred;
    }
    
}
