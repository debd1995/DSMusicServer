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

import com.kgp.debd.dsmusicserver.exception.FileStreamingException;
import com.kgp.debd.dsmusicserver.exception.FileUploadException;
import com.kgp.debd.dsmusicserver.exception.InvalidConfigurationException;
import com.kgp.debd.dsmusicserver.model.Audio;
import com.kgp.debd.dsmusicserver.model.AudioWithID;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author debd
 */
public class MediaDBHandler {
    
    private Connection connection = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    private String AUDIO_TABLE_NAME = "AUDIO";
    private String TRACK_TABLE_NAME = "TRACK";
    
    private String query_get_audio_list = "SELECT AUDIO_ID, NAME FROM "+AUDIO_TABLE_NAME+" WHERE USERNAME = ?";
    
    private String query_insert_audio = "INSERT INTO "+AUDIO_TABLE_NAME+" (AUDIO_ID, "
            + "NAME, ALBUM, USERNAME, ARTIST, DATE_CREATED, DURATION, GENRES, "
            + "REL_FILE_PATH, YEAR) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private String query_get_last_audioID = "SELECT AUDIO_COUNT FROM "
            
            +TRACK_TABLE_NAME+" WHERE USERNAME = ?";
    
    private String query_update_audio_track = "UPDATE "+TRACK_TABLE_NAME
            +" SET AUDIO_COUNT = ? WHERE USERNAME = ?";
    
    private String query_get_audioName_by_audioID = "SELECT NAME FROM "+AUDIO_TABLE_NAME+" WHERE AUDIO_ID = ?";
    
    private int updateAudioCount(int newCount, String username) throws FileUploadException{
        int in1 = 0;
        try{
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_update_audio_track);
            pstmt.setInt(1, newCount);
            pstmt.setString(2, username);
            
            in1 = pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ex.getMessage());
        }finally {
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Failed to close pstmt");
                }
            }
        }
        return in1;
    }
    
    public int getLastAudioCount(String username) throws FileUploadException{
        int id = -1;
        try {
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_get_last_audioID);
            pstmt.setString(1, username);
            
            rs = pstmt.executeQuery();
            if(rs.next()) {
                id = rs.getInt("AUDIO_COUNT");
                updateAudioCount(id+1, username);
            }                      
            
        } catch (SQLException ex) {
            throw new FileUploadException(ex.getMessage());
        } catch (IOException ex) {
            throw new FileUploadException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new FileUploadException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new FileUploadException(ex.getMessage());
        }finally {
            try {
                if(pstmt != null)
                    pstmt.close();
                if(rs != null)
                    rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Failed to close pstmt and rs");
            }
        }        
        return id;
    }
    
    public boolean insert(Audio audio) throws FileUploadException {
        
        boolean success = false;
        String audioID = audio.getAudioID();
        String audioName = audio.getName();
        
        if(audioID == null || audioName == null)
            throw new FileUploadException("Audio ID and Name must supply");
        try{
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_insert_audio);
            pstmt.setString(1, audioID);
            pstmt.setString(2, audioName);
            pstmt.setString(3, audio.getAlbum());
            System.out.println("ALBUM : "+audio.getAlbum());
            pstmt.setString(4, audio.getUsername());
            pstmt.setString(5, audio.getArtist());
            pstmt.setString(6, audio.getDateCreated());
            pstmt.setLong(7, audio.getDuration());
            pstmt.setString(8, audio.getGenres());
            pstmt.setString(9, audio.getRelFilePath());
            pstmt.setInt(10, audio.getYear());
            
            int in1 = pstmt.executeUpdate();
            if(in1 > 0)
                success = true;
            
        } catch (SQLException ex) {
            throw new FileUploadException(ex.getMessage());
        } catch (IOException ex) {
            throw new FileUploadException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new FileUploadException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new FileUploadException(ex.getMessage());
        }finally {            
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Failed to close pstmt");
                }
            }                        
        }
        
        return success;
    }
    
    public List<AudioWithID> getAudioList(String username) throws RuntimeException{
        List<AudioWithID> audioList = null;
        AudioWithID aid = null;
        
        
        try {
            connection = ConnectDB.connect();
            pstmt = connection.prepareStatement(query_get_audio_list);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            audioList = new ArrayList<AudioWithID>();
            
            while(rs.next()) {
                aid = new AudioWithID(rs.getString("AUDIO_ID"), rs.getString("NAME"));
                audioList.add(aid);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }finally {            
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Failed to close pstmt");
                }
            }                        
        }
        return audioList;
    }
    
    public String getAudioNameByID(String audioID) throws FileStreamingException{
        
        String audioName = null;
        
        try {
            Connection conn = ConnectDB.connect();
            
            pstmt = conn.prepareStatement(query_get_audioName_by_audioID);
            pstmt.setString(1, audioID);        
            rs = pstmt.executeQuery();
        
            if(rs.next()) {
                audioName = rs.getString("NAME");
            }
        }catch (SQLException ex) {
            throw new FileStreamingException(ex.getMessage());
        } catch (IOException ex) {
            throw new FileStreamingException(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            throw new FileStreamingException(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new FileStreamingException(ex.getMessage());
        }
        
        
        
        return audioName;    
    }
}
