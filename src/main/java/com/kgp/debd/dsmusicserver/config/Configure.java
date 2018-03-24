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
package com.kgp.debd.dsmusicserver.config;

import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import com.kgp.debd.dsmusicserver.model.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author debd
 */
public class Configure {
    
    private static final String configFile = "dsmusic.properties";
    private static final String configPath = System.getProperty("user.home")+File.separator+".DSMusic";
    private static final String config = configPath+File.separator+configFile;
    
    private static File file = null;
    private static FileOutputStream fout = null;
    private static Properties prop = null;
    
    public static boolean isConfigured(){
        return new File(config).exists();
    }
    
    public static synchronized void deleteConfiguration() {
        File basefile = new File(config);
        boolean succ = false;
        if(basefile.exists()){
            basefile.setWritable(true, true);
            succ = basefile.delete();
        }            
        System.out.println("Deleteing Base file from delete method.. called due to some error. status :  "+succ);
    }
    //overrides any old configuration file.
    public static synchronized boolean configureProperty(Configuration configuration) throws FileNotFoundException, IOException {
        boolean success = false;
        file = new File(config);  
        File baseDir = new File(configPath);
        
        // if file exists. then check for write (delete is writting ) permission.. 
        // If not then set write permission and then delete it.
        if(file.exists()) {
            System.out.println("File EXISTS/////");
            if(!file.canWrite())
                file.setWritable(true, true);
            file.delete();
            System.out.println("File DELETED////");
        }else
            System.out.println("File not exists......");
        
        if(baseDir.exists()) {
            System.out.println("Directory exists...");
            if(!baseDir.canWrite())
                baseDir.setWritable(true, true);
            baseDir.delete();
            System.out.println("Directory deleted....");
        }else
            System.out.println("Directory not exists....");
        
        try {
            // create new file and put propreties info
            boolean dirSuccess = baseDir.mkdirs();
            if(dirSuccess || baseDir.exists()) {
                System.out.println("Directory Created..///");
                boolean fileSucces = file.createNewFile();
                if(fileSucces) {
                    System.out.println("File created.......");
                    fout = new FileOutputStream(file);
                    prop = new Properties();
            
                    prop.setProperty("database", configuration.getDatabase());
                    prop.setProperty("host", configuration.getHost());
                    prop.setProperty("port", Integer.toString(configuration.getPort()));
                    prop.setProperty("username", configuration.getUsername());
                    prop.setProperty("password", configuration.getPassword());
            
                    //save those details..
                    prop.store(fout, "Database Details.");
                    success = true;
                    
                    // set permission to false for all users..
                    file.setReadable(false, false);
                    file.setWritable(false, false);
                    file.setExecutable(false, false);
                    
                    // setting read and write permission to only owner
                    file.setReadable(true, true);
                    file.setWritable(true, true);
                    
                }
            }
            
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Error. Configuration file not Found. Or the system has failed to create the file : " +ex.getMessage());
        } catch(IOException e) {
            throw new IOException("Error Creating configuration file. : "+e.getMessage());
        }finally {
            if(fout != null)
                fout.close();
            prop = null;
            file = null;
        }
        return  success;
    }
    
    public static Configuration getConfiguration() throws FileNotFoundException, IOException, InvalidConfigurationException {
        Configuration configuration = null;
        Properties prop = new Properties();
        try {
            FileInputStream fin = new FileInputStream(new File(config));
            prop.load(fin);
            configuration = new Configuration(prop.getProperty("database"), 
                                                prop.getProperty("host"), 
                                                Integer.parseInt(prop.getProperty("port")), 
                                                prop.getProperty("username"), 
                                                prop.getProperty("password"));
                        
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Configuration file not found : "+ex.getMessage());
        } catch (IOException ex) {
            throw new IOException("Failed to read Properties file : "+ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new InvalidConfigurationException("All Parameters are not found in configuration file.");
        } catch(NumberFormatException e) {
            throw new NumberFormatException("Cant read Port Value : "+e.getMessage());
        }
        
        return configuration;
    }
}
