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
package com.kgp.debd.dsmusicserver.filter;

import com.kgp.debd.dsmusicserver.annotation.CustomAuthorization;
import com.kgp.debd.dsmusicserver.exception.AuthorizationFailedException;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author debd
 */

@Provider
@Priority(Priorities.AUTHORIZATION)
@CustomAuthorization
public class Authorization implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        UriInfo uriInfo =  requestContext.getUriInfo();
        SecurityContext securityContext = requestContext.getSecurityContext();
        
        String unameFromContext = securityContext.getUserPrincipal().getName();
        String unamePath = uriInfo.getPath().toString().split("/")[1];
        
        System.out.println("URIFONFO: getPath " +uriInfo.getPath());
        System.out.println("URIFONFO: absulutePath " +uriInfo.getAbsolutePath());
        System.out.println("URIFONFO: baseURI " +uriInfo.getBaseUri());
        
        if(unameFromContext.equals(unamePath))
            return;
        else
            throw new AuthorizationFailedException("Not authorize to access this resource");
    }

}
