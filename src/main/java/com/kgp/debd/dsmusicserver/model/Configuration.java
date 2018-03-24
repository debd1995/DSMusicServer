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

import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import java.io.Serializable;

/**
 *
 * @author debd
 */
public class Configuration implements Serializable{
    private String database, host, username, password;
    private int port;

    public Configuration() {}

    public Configuration(String database, String host, int port, String username, String password) throws InvalidConfigurationException {
        if(database != null && host != null && port != 0 && username != null & password != null) {
            this.database = database;
            this.host = host;
            this.username = username;
            this.password = password;
            this.port = port;
        }else
            throw new InvalidConfigurationException("Some parameter are missing");
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }
        
    public boolean validate() {
        boolean success = false;
        
        if(database != null && host != null && port != 0 && username != null && password != null)
            success = true;
        
        return success;
    }       
}