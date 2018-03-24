<%-- 
    Document   : home
    Created on : 23 Aug, 2017, 4:54:46 PM
    Author     : debd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content=" width = device-width, initial-scale = 1">
        
        <link rel="stylesheet" href='<%= org.webjars.AssetLocator.getWebJarPath("css/bootstrap.min.css")%>'>
        <script type="text/javascript" src='<%= org.webjars.AssetLocator.getWebJarPath("jquery.min.js")%>'> </script>
        <script type="text/javascript" src='<%= org.webjars.AssetLocator.getWebJarPath("js/bootstrap.min.cs")%>'> </script>
        
        <title>DSMusic Home</title>
    </head>
    <body>
        
        <div class="container">
            <h1>DSMusic <small>Your Personal Music Streaming Service</small> </h1>
            <h2>Server Control Panel</h2>
            
            <p><a href="${pageContext.request.contextPath}/UploadAudio">Upload Audio</a></p>
        </div>
        
    </body>
</html>
