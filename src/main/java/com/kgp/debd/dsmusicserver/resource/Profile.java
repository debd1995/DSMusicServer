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
import com.kgp.debd.dsmusicserver.annotation.CustomAuthentication;
import com.kgp.debd.dsmusicserver.annotation.CustomAuthorization;
import com.kgp.debd.dsmusicserver.exception.UserNotFoundException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.kgp.debd.dsmusicserver.model.User;

/**
 *
 * @author debd
 */
@CustomAuthentication
@CustomAuthorization
public class Profile {
    
    @Path("checkLogin")
    @GET
    public Response checkLogin(@PathParam("uname") String username) {
        UserDBHandler userDBHandler = new UserDBHandler();
        String name = userDBHandler.getName(username);
        
       return Response.ok(name, MediaType.TEXT_PLAIN).build();
    }
    
    @GET
    @Path("profileDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@PathParam("uname") String uname){
        //return "sub resource..NAME : "+uname;
        UserDBHandler userDBHandler = new UserDBHandler();
        User user = userDBHandler.getUserDetails(uname);
        if(user == null) {
            throw new UserNotFoundException("User "+uname+" not found");
        }
        return Response.status(Response.Status.OK).entity(user).build();
        
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteProfile(@PathParam("uname") String uname) {
        return "Profile associated with User : "+uname+" Deleted.";
    }
    
    
    
}
