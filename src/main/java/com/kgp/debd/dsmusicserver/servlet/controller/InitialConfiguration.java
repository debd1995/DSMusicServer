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
package com.kgp.debd.dsmusicserver.servlet.controller;

import com.kgp.debd.dsmusicserver.DBHandler.util.InitialyzeDatabase;
import com.kgp.debd.dsmusicserver.config.Configure;
import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import com.kgp.debd.dsmusicserver.model.Configuration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author debd
 */
public class InitialConfiguration extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Configuration configuration = null; //model    
        boolean success = false;
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
                
        String dbname = null, host = null, username = null, password = null;
        int port = 0;
        
        try {
            if(req.getParameter("database") != null)
                dbname = req.getParameter("database");
        
            if(req.getParameter("dbHost") != null)
                host = req.getParameter("dbHost");
        
            if(req.getParameter("dbPort") != null )
                port = Integer.parseInt(req.getParameter("dbPort"));
        
            if(req.getParameter("dbUsername") != null)
                username = req.getParameter("dbUsername");
        
            if(req.getParameter("dbPassword") != null)
                password = req.getParameter("dbPassword");
            
            configuration = new Configuration(dbname, host, port, username, password);
            Configure.configureProperty(configuration); // creating .properties file
            InitialyzeDatabase.init(); // creating database
            out.print("<p>Succesfully Initialized</p> <p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            
            
        }catch(NumberFormatException e){
            out.print(e.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            e.printStackTrace();
            Configure.deleteConfiguration();
        }catch(InvalidConfigurationException e){
            out.print(e.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            e.printStackTrace();
            Configure.deleteConfiguration();
        }catch(FileNotFoundException e){
            out.print(e.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            e.printStackTrace();
            Configure.deleteConfiguration();
        }catch(IOException e){
            out.print(e.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            e.printStackTrace();
            Configure.deleteConfiguration();
        }catch(SQLException e){
            out.print(e.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            e.printStackTrace();
            Configure.deleteConfiguration();
        } catch (ClassNotFoundException ex) {
            out.print(ex.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            ex.printStackTrace();
            Configure.deleteConfiguration();
        } catch(Exception e) {
            out.print(e.getMessage()+"<p>Go <a href='"+req.getContextPath()+"/Home'>Home</a> </p>");
            e.printStackTrace();
            Configure.deleteConfiguration();
        }
    }
    
}
