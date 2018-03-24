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
package com.kgp.debd.dsmusicserver.exception.mapper;

import com.kgp.debd.dsmusicserver.model.Error;
import com.kgp.debd.dsmusicserver.exception.AuthorizationFailedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author debd
 */

@Provider
public class AuthorizationFailedExceptionMapper implements ExceptionMapper<AuthorizationFailedException>{

    @Override
    public Response toResponse(AuthorizationFailedException e) {
        Error error = new Error(e.getMessage(), "This is a protected resource. You do not have permission to access this", "review your permission on this resource", Error.AUTHORIZATION_ERROR);
        return Response.status(Error.AUTHORIZATION_ERROR).entity(error).build();
    }
    
}
