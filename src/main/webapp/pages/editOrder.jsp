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
        <h1>View/Update Order</h1>
        <hr>

        <ol class="breadcrumb">
            <li class="breadcrumb-item active">Order Details</li>
        </ol>

        <div class="panel-body">
            <div class="row">
                <div class="col-lg-5 bottom-buffer">

                    <form role="form" method="post" action="<%=request.getContextPath()%>/singleOrder.do" id="orderForm">

                        <div class="form-group row">
                            <label for="orderNo" class="col-sm-4 col-form-label">Order No</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="orderNo" name="orderNo" value="${order.orderId}" readonly>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="reference" class="col-sm-4 col-form-label">Item</label>
                            <div class="col-sm-8">
                                <input class="form-control" type="text" id="reference" name="reference" value="${order.sku}" readonly>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="itemQuantity" class="col-sm-4 col-form-label">Quantity</label>
                            <div class="col-sm-8">
                                <input type="number" class="form-control" id="itemQuantity" name="itemQuantity" min="1" max="50" step="1" value="${order.quantity}" />
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bName" class="col-sm-4 col-form-label">Name</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="bName" name="bName" value="${order.buyerName}">
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bAddress" class="col-sm-4 col-form-label">Address</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="bAddress" name="bAddress" value="${order.street}">
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bApt" class="col-sm-4 col-form-label">Address2/Apt</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="bApt" name="bApt" value="${order.street2}">
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bCiti" class="col-sm-4 col-form-label">City</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="bCiti" name="bCiti" value="${order.city}">
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bState" class="col-sm-4 col-form-label">State</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="bState" name="bState" value="${order.state}">
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bZip" class="col-sm-4 col-form-label">Zip code</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="bZip" name="bZip" value="${order.zipCode}">
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bCountry" class="col-sm-4 col-form-label">Country</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="bCountry" name="bCountry" value="${order.country}" >
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="bNotes" class="col-sm-4 col-form-label">Observations</label>
                            <div class="col-sm-8">
                                <textarea class="form-control" id="bNotes" name="bNotes" value="${order.observations}">${order.observations}</textarea>
                            </div>
                        </div>

                        <input type="hidden" name="market" value="${order.marketId}">
                        <button type="submit" class="btn btn-primary mb-2 float-right" name="submit">Submit</button>

                    </form>

                </div>
                <!-- /.col-lg-6 (nested) -->
                <div class="col-lg-3"/>
                <div class="col-lg-4"/>
                <!-- /.col-lg-6 (nested) -->
            </div>
            <!-- /.row (nested) -->
        </div>

    </div>
    <jsp:include page="footer.jsp"/>

    <%--Form Validation - https://jqueryvalidation.org/ --%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/vendor/jquery-validation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/vendor/jquery-validation/myformvalidation.js"></script>

</div>

</body>
</html>
