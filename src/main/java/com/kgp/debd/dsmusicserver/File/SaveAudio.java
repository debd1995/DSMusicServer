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
package com.kgp.debd.dsmusicserver.File;

import com.kgp.debd.dsmusicserver.DBHandler.MediaDBHandler;
import com.kgp.debd.dsmusicserver.exception.FileUploadException;
import com.kgp.debd.dsmusicserver.model.Audio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author debd
 */
public class SaveAudio {
    
    private FileOutputStream fout = null;
    private File outFile = null;
    
    private MediaDBHandler mediaDBH = new MediaDBHandler();
    
    private final String rootPathToUpload = 
            System.getProperty("user.home")+File.separator+".DSMusic"+File.separator+"uploaded"
            +File.separator+"audio";  
    
    
    
    
    
    public boolean save(InputStream in, String fileName, String username) throws FileUploadException{
        
         File uploadLocation = new File(rootPathToUpload+File.separator+username);
        
        if(!uploadLocation.exists())
            uploadLocation.mkdirs();
        
        //String fileName = uploadedFileDetails.getFileName();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        
        System.out.println("UPLOAD LOCATION : "+uploadLocation);
        System.out.println("FILE NAME : "+fileName);
        System.out.println("FILE EXTENSION : "+fileType);
        
        if(!fileType.equalsIgnoreCase("mp3"))
            throw new FileUploadException("File type ."+fileType+" is not supported");
        
        String savedFileLoc = saveFileToDisk(in, uploadLocation+File.separator+fileName);
        
        System.out.println("FILE SAVED LOCATION : "+savedFileLoc);
        
        if(savedFileLoc == null)
            throw new FileUploadException("Failed to save to disk");
        
        boolean success = saveFileDetailsToDB(username, fileName, savedFileLoc);
        
        return success;
        
    }
    
    
    
    
    private String saveFileToDisk(InputStream in, String absolutePath) throws FileUploadException{
        String loc = null;
        try {
            
            outFile = new File(absolutePath);
            fout = new FileOutputStream(outFile);
            
            IOUtils.copy(in, fout);
            
            loc = absolutePath;
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ex.getMessage());
        }finally {
            if(fout != null) {
                try {
                    fout.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }            
        }        
        return loc;
    }
    
    
    private boolean saveFileDetailsToDB(String username, String fileName, String fileLocation) throws FileUploadException{
        boolean success = false;
        Audio audio = new Audio();
        FileInputStream fin = null;
        int lastAudioCount = mediaDBH.getLastAudioCount(username);
        
        // format :  USERNAME+AUDIO+(Lastcount+1)
        String audioID = username+"audio"+Integer.toString(lastAudioCount+1);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();        
        
        try {
            fin = new FileInputStream(new File(fileLocation));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(fin, handler, metadata, parseCtx);
            
            audio.setAudioID(audioID);
            audio.setName(fileName);
            audio.setAlbum(metadata.get("xmpDM:album"));
            audio.setArtist(metadata.get("xmpDM:artist"));
            audio.setGenres(metadata.get("xmpDM:genre"));
            audio.setUsername(username);
            audio.setDateCreated(dtf.format(localDate));
            
            success = mediaDBH.insert(audio);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage());
        }catch(IOException e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage());
        }catch(SAXException e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage());
        }catch(TikaException e) {
            e.printStackTrace();
            throw new FileUploadException(e.getMessage());
        }finally {
            if(fin != null){
                try {
                    fin.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Failed to close Input Stream");
                }
            }
        }                        
        return success;
    }
    
    
    
}
