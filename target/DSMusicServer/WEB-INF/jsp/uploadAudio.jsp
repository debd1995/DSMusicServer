<%-- 
    Document   : uploadAudio
    Created on : 19 Oct, 2017, 1:59:18 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload Audio</title>
    </head>
    <body>
        
        <div class="div_audio_upload_form">
            <form action="${pageContext.request.contextPath}/api/user/debd/audio" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" class="form-control" name="username">
                </div>
                
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" class="form-control" name="password">
                </div>
                
                <div class="form-group">
                    <label>Choose Audio file</label>
                    <input type="file" class="form-control" name="file">
                </div>
                <button type="submit" class="btn btn-default">Upload</button>
            </form>                        
        </div>
        
        
    </body>
</html>
