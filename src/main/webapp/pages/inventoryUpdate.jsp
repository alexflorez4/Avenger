<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Avenger</title>
        <jsp:include page="header.jsp"/>
    </head>

    <body class="fixed-nav sticky-footer bg-dark" id="page-top">
        <jsp:include page="navigation.jsp"/>

        <div class="content-wrapper">
            <div class="container-fluid">
                <h1>Inventory Management</h1>
                <hr>
                <!-- Breadcrumbs-->
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">AZ importer</li>
                </ol>

                <p>Upload AZ importer file</p>

                <form class="form-inline" method="POST" action="processAZImportFile.do" enctype="multipart/form-data">
                    <div class="form-group mx-sm-3 mb-2">
                        <input type="file" class="form-control" id="azFile" name="azFile">
                    </div>
                    <button type="submit" class="btn btn-primary mb-2">Submit</button>
                </form>
            </div>
            <div class="container-fluid">
                <hr>
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">Fragrance X</li>
                </ol>

                <p>Click to Update Inventory</p>

                <form class="form-inline" method="POST" action="processFragXInventory.do">
                    <button type="submit" class="btn btn-primary mb-2">Update</button>
                </form>
            </div>
            <!-- /.container-fluid-->
            <jsp:include page="footer.jsp"/>
        </div>
        <!-- /.content-wrapper-->
    </body>
</html>

