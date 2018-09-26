<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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
                    <li class="breadcrumb-item active">Inventory File Upload: <b>SKU | Price | Quantity | Weight Per Unit</b></li>
                </ol>

                <c:if test="${status == 'success'}">
                    <div class="alert alert-success">
                        Inventory Updated Successfully!
                    </div>
                </c:if>
                <c:if test="${fn:contains(status, 'Error')}">
                    <div class="alert alert-danger">
                            ${status}
                    </div>
                </c:if>

                <form method="POST" action="inventoryUpdate.do" enctype="multipart/form-data" id="inventoryUpload">
                    <div class="form-group row">
                        <label for="azFile" class="col-sm-1 col-form-label">File</label>
                        <div class="col-sm-3">
                            <input type="file" class="form-control" id="azFile" name="azFile">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="supplier" class="col-sm-1 col-form-label">Supplier</label>
                        <div class="col-sm-3">
                            <select class="form-control" id="supplier" name="supplier">
                                <option></option>
                                <option value="500">AZ Trading</option>
                                <option value="501">Fragrance X</option>
                                <option value="502">Teledynamics</option>
                                <option value="503">Perfume World Wide</option>
                                <option value="599">Shades</option>
                            </select>
                        </div>
                    </div>
                    <div class="container-fluid form-group row">
                        <button type="submit" class=" btn btn-primary">Update</button>
                    </div>
                </form>
            </div>
            <div class="container-fluid">
                <hr>
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">Fragrance X</li>
                </ol>


                <c:if test="${fragStatus == 'success'}">
                    <div class="alert alert-success">
                        Inventory Updated Successfully!
                    </div>
                </c:if>
                <c:if test="${fn:contains(fragStatus, 'Error')}">
                    <div class="alert alert-danger">
                        ${fragStatus}
                    </div>
                </c:if>

                <p>Click to Update Inventory</p>

                <form class="form-inline" method="POST" action="processFragXInventory.do">
                    <button type="submit" class="btn btn-primary mb-2">Update</button>
                </form>
            </div>
            <!-- /.container-fluid-->
            <jsp:include page="footer.jsp"/>
            <%--Form Validation - https://jqueryvalidation.org/ --%>
            <script type="text/javascript" src="<%=request.getContextPath()%>/resources/vendor/jquery-validation/jquery.validate.min.js"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/resources/vendor/jquery-validation/inventoryUpdateFormValidation.js"></script>
        </div>
        <!-- /.content-wrapper-->
    </body>
</html>

