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
package com.kgp.debd.dsmusicserver.resource;

import com.kgp.debd.dsmusicserver.DBHandler.UserDBHandler;
import com.kgp.debd.dsmusicserver.exception.SignupException;
import com.kgp.debd.dsmusicserver.exception.UsernameAlreadyExistsException;
import javax.ws.rs.Path;

import com.kgp.debd.dsmusicserver.model.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 *
 * @author debd
 */

@Path("signup")
public class Signup {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(User user) throws SignupException, UsernameAlreadyExistsException{
        
        String username,password,fName,lName,email;
        long phn;
        
        
        try {
            username = user.getUsername();
            password = user.getPassword();
            fName = user.getfName();
            lName = user.getlName();
            email = user.getEmail();
            phn = user.getPhoneNumber();
            
            System.out.println("++++++++++++++++++++\n\n"+username+"\n"+password+
                    "\n"+fName+"\n"+lName+"\n"+email+"\n"+phn+"\n\n++++++++++++++++++++");
            
            if(username == null || password == null || fName == null)
                throw new SignupException("username/ password/ First Name must not be empty.");
            
            UserDBHandler userDBHandler = new UserDBHandler();
            boolean success = userDBHandler.insert(user);
            if(success)
                return Response.status(Response.Status.CREATED).entity(user).build();
            else
                throw new SignupException("Signup process failed.");
             
        }catch(NullPointerException e) {
            e.printStackTrace();
            throw new SignupException(e.getMessage());
        }catch(SignupException e){
            e.printStackTrace();
            throw new SignupException(e.getMessage());
        }catch(UsernameAlreadyExistsException e){
            e.printStackTrace();
            throw new UsernameAlreadyExistsException(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new SignupException(e.getMessage());
        }        
    }
    
    @GET
    @Path("/format")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFormat() {
//        User user = new User(1234567890, "username", "password", "fName", "lName", "email");
//        return user;
        
        String format = "{\n" +
                        "  \"email\": \"your_email\",\n" +
                        "  \"fName\": \"Your_First_Name\",\n" +
                        "  \"lName\": \"Your_Last_Name\",\n" +
                        "  \"phoneNumber\": 1234567890,\n" +
                        "  \"username\": \"Your_username\",\n" +
                        "  \"password\": \"Your_password\"\n"+
                        "}";
         return format;
    }
}
