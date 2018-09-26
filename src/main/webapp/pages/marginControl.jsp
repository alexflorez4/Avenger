<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
        <h1>Margin Control</h1>
        <hr>

        <div class="panel-body">

            <div class="row">
                <div class="col-lg-12 bottom-buffer">
                    <div class="card mb-3">
                        <div class="card-header">
                            <i class="fa fa-table"></i> Adjust SKU Margin: <b>Sku | My Price | My Quantity</b> </div>
                            <div class="card-body">

                                    <c:if test="${fail} ">
                                        <div class="alert alert-danger">
                                            ${fail}
                                        </div>
                                    </c:if>

                                <form class="form-inline" method="POST" action="marginAdjuster.do" enctype="multipart/form-data">
                                    <div class="form-group mx-sm-3 mb-2">
                                        <input type="file" class="form-control" id="inventoryFile" name="inventoryFile">
                                    </div>
                                    <div class="form-group mx-sm-3 mb-2">
                                        <input type="number" class="form-control" id="percentage" name="percentage" min="1" max="50" step="1" value="1"/>
                                    </div>
                                    <button type="submit" class="btn btn-primary mb-2">Compare</button>
                                </form>
                            </div>
                        <div class="card-footer small text-muted"></div>
                    </div>
                </div>
            </div>
            <!-- /.row (nested) -->

            <div class="row">
                <div class="col-lg-12 bottom-buffer">
                    <div class="card mb-3">
                        <div class="card-header">
                            <i class="fa fa-table"></i> Get Missing listings: <b>Sku | Asin</b> </div>
                        <div class="card-body">

                            <c:if test="${fail} ">
                                <div class="alert alert-danger">
                                        ${fail}
                                </div>
                            </c:if>

                            <form class="form-inline" method="POST" action="expressLister.do" enctype="multipart/form-data">
                                <div class="form-group mx-sm-3 mb-2">
                                    <input type="file" class="form-control" id="asinFile" name="asinFile">
                                </div>
                                <button type="submit" class="btn btn-primary mb-2">Compare</button>
                            </form>
                        </div>
                        <div class="card-footer small text-muted"></div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="footer.jsp"/>

    <%--Form Validation - https://jqueryvalidation.org/ --%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/vendor/jquery-validation/jquery.validate.min.js"></script>
    <!-- Page level plugin JavaScript-->
    <script src="<%=request.getContextPath()%>/resources/vendor/datatables/jquery.dataTables.js"></script>
    <script src="<%=request.getContextPath()%>/resources/vendor/datatables/dataTables.bootstrap4.js"></script>
    <!-- Custom scripts for this page-->
    <script src="<%=request.getContextPath()%>/resources/js/sb-admin-datatables.min.js"></script>

</div>

</body>
</html>
