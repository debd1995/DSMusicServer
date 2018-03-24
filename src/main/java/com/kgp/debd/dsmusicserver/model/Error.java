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
package com.kgp.debd.dsmusicserver.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debd
 */

@XmlRootElement
public class Error implements Serializable{
    
    public static final int USERNAME_ALREADY_EXISTS = 460;
    public static final int AUTHENTICATION_ERROR = 461;
    public static final int AUTHORIZATION_ERROR = 462;
    public static final int USER_NOT_FOUND_ERROR = 463;
    public static final int GENERICS_ERROR = 500;
    public static final int SIGNUP_ERROR = 470;
    public static final int UPLOAD_ERROR = 471;
    public static final int STREAMING_ERROR = 480;
    
    private String responseMessage, description, suggession;
    private int responseCode;

    public Error() {}

    public Error(String responseMessage, String description, String suggession, int errorCode) {
        this.responseMessage = responseMessage;
        this.description = description;
        this.suggession = suggession;
        this.responseCode = errorCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSuggession() {
        return suggession;
    }

    public void setSuggession(String suggession) {
        this.suggession = suggession;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    
    
    
    
    
}
