<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
        <h1>Pending Orders</h1>
        <hr>

        <div class="panel-body">
            <div class="row">
                <div class="col-lg-12 bottom-buffer">

                    <div class="card mb-3">
                        <div class="card-header">
                            <i class="fa fa-table"></i> Orders </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <form role="form" method="post" action="<%=request.getContextPath()%>/stageOrder.do" id="orderForm">
                                    <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                        <thead>
                                        <tr>
                                            <th></th>
                                            <th>No</th>
                                            <th>Market Order id</th>
                                            <th>SKU</th>
                                            <th>Quantity</th>
                                            <th>Buyer Name</th>
                                            <th>Street</th>
                                            <th>Street2/Apt</th>
                                            <th>City</th>
                                            <th>State</th>
                                            <th>Zip Code</th>
                                            <th>Other</th>
                                            <th>Country</th>
                                            <th>Observations</th>
                                            
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${orders}" var="order">
                                            <tr>
                                                <th><input type="checkbox" name="orderToStage" value="${order.orderId}"></th>
                                                <td>${order.orderId}</td>
                                                <td>${order.marketOrderId}</td>
                                                <td>${order.sku}</td>
                                                <td>${order.quantity}</td>
                                                <td>${order.buyerName}></td>
                                                <td>${order.street}</td>
                                                <td>${order.street2}</td>
                                                <td>${order.city}</td>
                                                <td>${order.state}</td>
                                                <td>${order.zipCode}</td>
                                                <td>${order.other}</td>
                                                <td>${order.country}</td>
                                                <td>${order.observations}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                    <button type="submit" class="btn btn-primary mb-2 float-right" name="submit">Stage</button>
                                </form>
                            </div>
                        </div>
                        <div class="card-footer small text-muted">Be Happy ;)</div>
                    </div>

                </div>

            </div>
            <!-- /.row (nested) -->
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