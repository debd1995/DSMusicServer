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

import com.kgp.debd.dsmusicserver.DBHandler.UserDBHandler;
import com.kgp.debd.dsmusicserver.annotation.CustomAuthentication;
import com.kgp.debd.dsmusicserver.exception.AuthenticationFailedException;
import com.kgp.debd.dsmusicserver.exception.UnsupportedAuthenticationMethodException;
import com.kgp.debd.dsmusicserver.model.Error;
import com.kgp.debd.dsmusicserver.security.PBKDF2;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.glassfish.jersey.internal.util.Base64;

/**
 *
 * @author debd
 */

@Provider
@Priority(Priorities.AUTHENTICATION)
@CustomAuthentication
public class Authentication implements ContainerRequestFilter{
    private ContainerRequestContext requestContext = null;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{
        
        this.requestContext = requestContext;
        List<String> auth =  requestContext.getHeaders().get(HttpHeaders.AUTHORIZATION);
        
        SecurityContext securityContext = requestContext.getSecurityContext();
        securityContext = null;
        
        
        if(auth != null) {
            System.out.println("AUTH LIST : "+auth+"\nEND LIST");
            System.out.println("Auth.get(0) : "+auth.get(0)+"\nEND auth.get(0)");
        
            String authPara[] = auth.get(0).split(" ");
        
            System.out.println("0 --> "+authPara[0]);
            System.out.println("1 --> "+authPara[1]);
        
            if(authPara[0].equalsIgnoreCase("customAuth"))
                System.out.println("Custom Auth : "+authPara[1]);
            else if(authPara[0].equalsIgnoreCase("Basic"))
                System.out.println("Basic Auth : "+authPara[1]);

            
            String authType = auth.get(0).split(" ")[0];
            switch(authType) {
                
                case "Basic" :
                    String encodedString = auth.get(0).split(" ")[1];
                    String decodedString = Base64.decodeAsString(encodedString);
                    String[] cred = decodedString.split(":");
                    String uname = cred[0];
                    String pass = cred[1];                    
                    boolean success = validate(uname, pass);
                    
                    if(success){
                        requestContext.setSecurityContext(new SecurityContext() {
                            @Override
                            public Principal getUserPrincipal() {
                                return new Principal() {
                                    @Override
                                    public String getName() {
                                        return uname;
                                    }
                                };
                            }

                            @Override
                            public boolean isUserInRole(String string) {
                                
                                //implement later
                                //
                                return false;
                            }

                            @Override
                            public boolean isSecure() {
                                return requestContext.getUriInfo().getAbsolutePath().toString().startsWith("https");
                            }

                            @Override
                            public String getAuthenticationScheme() {
                                return "Basic";
                            }
                        });
                        return;
                    }                        
                    else{
                        throw new AuthenticationFailedException("Username or Password does not matched");
//                        Error error = new Error(e.getMessage(), "Error while authenticating user", "Check your username and Password", Error.AUTHENTICATION_ERROR);
//                        abortWith(requestContext, Response.status(Error.AUTHENTICATION_ERROR).entity(error).build());
                    }
                    
                    //break;
                default :
                    throw new UnsupportedAuthenticationMethodException("Authentication method: "+authType+" method not supported");
            }
            
            
        }else
            throw new AuthenticationFailedException("No credentials");
    }
    
    private boolean validate(String username, String password){
        System.out.println("\n\nusername :"+username+"\npassword : "+password+"\n\n");
        
        boolean success = false;
        String goodHash = null;
        
        try {
            goodHash = new UserDBHandler().getCredential(username);
            if(goodHash == null)
                return false;
            
            success = PBKDF2.validatePassword(password, goodHash);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new AuthenticationFailedException(ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
            throw new AuthenticationFailedException(ex.getMessage());
        } catch (AuthenticationFailedException ex) {
            ex.printStackTrace();
            throw new AuthenticationFailedException(ex.getMessage());
        }
        
        return success;
    }
    
 
    
}
