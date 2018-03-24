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
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debd
 */
@XmlRootElement
public class Audio implements Serializable{
    
    private String audioID, name, album, username, artist, genres, relFilePath, dateCreated;
    private int year;    
    private long duration;
    public Audio() {}

    public Audio(String audioID, String name, String album, String username, String artist, String genres, String relFilePath, int year, String dateCreated, long duration) {
        this.audioID = audioID;
        this.name = name;
        this.album = album;
        this.username = username;
        this.artist = artist;
        this.genres = genres;
        this.relFilePath = relFilePath;
        this.year = year;
        this.dateCreated = dateCreated;
        this.duration = duration;
    }

    public String getAudioID() {
        return audioID;
    }

    public void setAudioID(String audioID) {
        this.audioID = audioID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRelFilePath() {
        return relFilePath;
    }

    public void setRelFilePath(String relFilePath) {
        this.relFilePath = relFilePath;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    
    
    
    
}
