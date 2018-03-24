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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author Debasish Nandi
 */

@Path("user")
@CustomAuthentication
@CustomAuthorization
public class User {
        
    @Path("{uname}/profile")    
    public Profile profile(){
       return new Profile();
    }
    
    
    @Path("{uname}/audio")    
    public Audio audio(){
        return new Audio();
    }    
    
}
