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
package com.kgp.debd.dsmusicserver.resource;


import com.kgp.debd.dsmusicserver.DBHandler.MediaDBHandler;
import com.kgp.debd.dsmusicserver.File.MediaStreamer;
import com.kgp.debd.dsmusicserver.File.SaveAudio;
import com.kgp.debd.dsmusicserver.annotation.CustomAuthentication;
import com.kgp.debd.dsmusicserver.annotation.CustomAuthorization;
import com.kgp.debd.dsmusicserver.exception.FileStreamingException;
import com.kgp.debd.dsmusicserver.exception.FileUploadException;
import com.kgp.debd.dsmusicserver.model.AudioWithID;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Debasish Nandi
 */
@CustomAuthentication
@CustomAuthorization
public class Audio {            
    
    private List<AudioWithID> audioList = null;
    private MediaDBHandler mediaDBH = null;
    
    private final int chunk_size = 1024 * 1024; // 1MB chunks
    private File audio;
    
    private final String rootPathToStream = 
            System.getProperty("user.home")+File.separator+".DSMusic"+File.separator+"uploaded"
            +File.separator+"audio";
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)    
    public List<AudioWithID> getAudioList(@PathParam("uname") String uname) throws RuntimeException {
        
        mediaDBH = new MediaDBHandler();
        audioList = mediaDBH.getAudioList(uname);
       
        return audioList;
    }
    
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadAudio(
                                @FormDataParam("file") InputStream uploadedInputStream,
                                @FormDataParam("file") FormDataContentDisposition uploadedFileDetails,
                                @PathParam("uname") String username) throws FileUploadException {
        SaveAudio saveAudio = new SaveAudio();
       try {
           boolean success = saveAudio.save(uploadedInputStream, uploadedFileDetails.getFileName(), username);
           
           if(success)
               return Response.status(Response.Status.CREATED).entity("File successfully uploaded").build();
           else
               return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Failed to upload").build();
           
       }catch(FileUploadException e){
           e.printStackTrace();
           throw new FileUploadException(e.getMessage());
       }
        
    }        
    
    @Path("{audioID}")
    @GET
    @Produces("audio/mpeg")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response streamAudio(
                @HeaderParam("Range") String range, 
                @PathParam("uname") String username,
                @PathParam("audioID") String audioID                                       
            ) throws IOException, FileStreamingException {
        
        File streamLocation = new File(rootPathToStream+File.separator+username);
        MediaDBHandler mdbh = new MediaDBHandler();
        
        if(!streamLocation.exists())
            return Response.status(Response.Status.NOT_FOUND).entity("Directory not found").build();
        
        String name = mdbh.getAudioNameByID(audioID);
        
        if(name == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Requested File Not Found").build();
        
        
        
        String streamFile = rootPathToStream + File.separator + username + File.separator + name;
        File audioFile = new File(streamFile);
        
        if(!audioFile.exists())
            return Response.status(Response.Status.NOT_FOUND).entity(name + " Not Found").build();
        
        //URL url = this.getClass().getResource(MEDIA_FILE);
        
        return startStream(audioFile, range);
        
    }

    private Response startStream(File fileToStream, String range) throws FileNotFoundException, IOException {
        
        if (range == null) {
            StreamingOutput streamer;
            streamer = output -> {
                try (FileChannel inputChannel = new FileInputStream(fileToStream).getChannel();
                        WritableByteChannel outputChannel = Channels.newChannel(output)) {
                    
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                }
            };
            return Response.ok(streamer).status(200).header(HttpHeaders.CONTENT_LENGTH, fileToStream.length()).build();
        }
        
        
        String[] ranges = null;
        int from = 0;
        
        ranges = range.split("=");
        System.out.println("range data present in header");
        for(String s: ranges)
            System.out.println(s);
        System.out.println("--------------");
        // determining range fromat and determining ranges
        
        if(ranges.length == 1) {
            ranges = range.split("-");
            from = Integer.parseInt(ranges[0]);
        }else {
            ranges = ranges[1].split("-");
            from = Integer.parseInt(ranges[0]);
        }
                                
//        String[] ranges = range.split("=")[1].split("-");
//        final int from = Integer.parseInt(ranges[0]);

        /*
          Chunk media if the range upper bound is unspecified. Chrome, Opera sends "bytes=0-"
         */
        int to = chunk_size + from;
        if (to >= fileToStream.length()) {
            to = (int) (fileToStream.length() - 1);
        }
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        }

        System.out.println("FROM --> "+from+"\nTO --> "+to+"\n---------------------");
        
        final String responseRange = String.format("bytes %d-%d/%d", from, to, fileToStream.length());
        final RandomAccessFile raf = new RandomAccessFile(fileToStream, "r");
        raf.seek(from);

        final int len = to - from + 1;
        final MediaStreamer streamer = new MediaStreamer(len, raf);
        Response.ResponseBuilder res = Response.ok(streamer)
                .status(Response.Status.PARTIAL_CONTENT)
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", responseRange)
                .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
                .header(HttpHeaders.LAST_MODIFIED, new Date(fileToStream.lastModified()));
        return res.build();
        
    }
       
}
