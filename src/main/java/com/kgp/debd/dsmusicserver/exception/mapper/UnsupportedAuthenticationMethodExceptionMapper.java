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
import com.kgp.debd.dsmusicserver.exception.UnsupportedAuthenticationMethodException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author debd
 */

@Provider
public class UnsupportedAuthenticationMethodExceptionMapper implements ExceptionMapper<UnsupportedAuthenticationMethodException>{

    @Override
    public Response toResponse(UnsupportedAuthenticationMethodException e) {
        Error error = new Error(e.getMessage(), "This method of authentication is not sopported", "Try another method of authentication.", Error.AUTHENTICATION_ERROR);
        return Response.status(Error.AUTHENTICATION_ERROR).entity(error).build();
    }
    
}
