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

import com.kgp.debd.dsmusicserver.config.Configure;
import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import com.kgp.debd.dsmusicserver.model.Configuration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author debd
 */
public class ConnectDB {
    private static String URL = null; 
    private static Connection conn = null;
    private static String dbName = "DSMUSIC";
    
    public static Connection connect() throws SQLException, IOException, FileNotFoundException, InvalidConfigurationException, ClassNotFoundException{
        
        Configuration configuration = Configure.getConfiguration();
        
        String driverName = "jdbc:"+configuration.getDatabase();
        URL = driverName+"://"+configuration.getHost()+":"+configuration.getPort()+"/"+dbName+
                "?user="+configuration.getUsername()+"&password="+configuration.getPassword();
        
        Class.forName("com.mysql.jdbc.Driver");
        if(conn != null)
            return conn;
        else
            conn = DriverManager.getConnection(URL);
        
        return conn;
    }
    
    public static Connection initConnect() throws SQLException, IOException, FileNotFoundException, InvalidConfigurationException, ClassNotFoundException{
        
        Configuration configuration = Configure.getConfiguration();
        Connection initConn = null;
        
        String driverName = "jdbc:"+configuration.getDatabase();
        URL = driverName+"://"+configuration.getHost()+":"+configuration.getPort()+
                "?user="+configuration.getUsername()+"&password="+configuration.getPassword();
        
        Class.forName("com.mysql.jdbc.Driver");
        if(initConn != null)
            return initConn;
        else
            initConn = DriverManager.getConnection(URL);
        
        return initConn;
    }
    
    
}
