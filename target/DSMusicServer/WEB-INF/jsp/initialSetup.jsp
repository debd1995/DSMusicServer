<%-- 
    Document   : initialSetup
    Created on : 23 Aug, 2017, 4:52:03 PM
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
        
        <title>DSMusic Server Setup Page</title>
    </head>
    <body>
        <div class="container">
            
            <h1>DSMUSIC Setup <small>initial configuration</small> </h1>
            <br> <br> <br>                                                
            
            <form class="form-horizontal" action="${pageContext.request.contextPath}/InitialConfiguration" method="POST">                
                <div class="form-group">
                    <div class="col-sm-4">
                        <hr>
                    </div>
                    <div class="col-sm-4" style="text-align: center;">
                        <label class="control-label">Database Information</label>
                    </div>
                    <div class="col-sm-4">
                        <hr>
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">Database</label>
                    </div>
                    <div class="col-sm-10">
                        <select name="database"><option value="mysql" >MySQL</option> </select>
                    </div>                    
                </div>
                
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">Host</label>
                    </div>
                    <div class="col-sm-10">
                        <input class="form-control" type="text" name="dbHost" placeholder="eg: 127.0.0.1">
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">Host Port</label>
                    </div>
                    <div class="col-sm-10">
                        <input class="form-control" type="text" name="dbPort" placeholder="eg: 3306">
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">Username</label>
                    </div>
                    <div class="col-sm-10">
                        <input class="form-control" type="text" name="dbUsername" placeholder="eg: mysql">
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">Password</label>
                    </div>
                    <div class="col-sm-10">
                        <input class="form-control" type="password" name="dbPassword" placeholder="eg: password">
                    </div>
                </div>
                
                <!--
                <div class="form-group">
                    <div class="col-sm-4">
                        <hr>
                    </div>
                    <div class="col-sm-4" style="text-align: center;">
                        <label class="control-label">Storage Information</label>
                    </div>
                    <div class="col-sm-4">
                        <hr>
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">Path for saving Audio</label>
                    </div>
                    <div class="col-sm-10">
                        <input class="form-control" type="text" name="FilePath" placeholder="eg: c:">                        
                    </div>
                </div>
                -->
                
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">GO</button>
                        <button type="reset" class="btn btn-default">Reset</button>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
