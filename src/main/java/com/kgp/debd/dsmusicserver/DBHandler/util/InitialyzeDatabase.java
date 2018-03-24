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
package com.kgp.debd.dsmusicserver.DBHandler.util;

import com.kgp.debd.dsmusicserver.DBHandler.ConnectDB;
import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author debd
 */
public class InitialyzeDatabase {
    
    
    private static Connection conn = null;
    
    
    public static boolean init() throws FileNotFoundException, SQLException, IOException, InvalidConfigurationException, ClassNotFoundException{ 
        
        boolean success = false;
        
        try{
            
            conn = ConnectDB.initConnect();
            ScriptRunner script = new ScriptRunner(conn, false, true);
            InputStream input = InitialyzeDatabase.class.getClassLoader().getResourceAsStream("sqlScript"+File.separator+"DSMusic_mysql.sql");
            script.runScript(new BufferedReader(new InputStreamReader(input)));
            success = true;
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
            throw new FileNotFoundException("SQL script file not found : "+e.getMessage());
        }catch(IOException e){
            throw new IOException("Error while reading sql script.");
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("Error while creating database or tables : "+e.getMessage());            
        } catch (InvalidConfigurationException ex) {
            throw new InvalidConfigurationException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new ClassNotFoundException("JDBC Driver Class not found : "+ex.getMessage());
        }finally {
            if(conn != null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                throw new SQLException("Failed to close Database connection");
            }
        }
        return success;
    }         
    
}
