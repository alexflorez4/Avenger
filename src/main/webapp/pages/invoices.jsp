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
        <h1>Invoices</h1>
        <hr>

        <div class="panel-body">

            <div class="row">
                <div class="col-lg-12 bottom-buffer">

                    <div class="card mb-3">
                        <div class="card-header">
                            <i class="fa fa-table"></i> Select Date Range </div>
                            <div class="card-body">

                                <form class="form-inline" method="POST" action="<%=request.getContextPath()%>/generateInvoice.do">
                                    <div class="form-group mx-sm-3 mb-2">
                                        <input class="form-control" type="text" id="from" name="from">
                                    </div>
                                    <p> to </p>
                                    <div class="form-group mx-sm-3 mb-2">
                                        <input class="form-control" type="text" id="to" name="to">
                                    </div>
                                    <button type="submit" class="btn btn-primary mb-2">Submit</button>
                                </form>
                            </div>
                        <div class="card-footer small text-muted">Be Happy ;)</div>
                    </div>
                </div>
            </div>
            <!-- /.row (nested) -->
        </div>

    </div>
    <jsp:include page="footer.jsp"/>


    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/vendor/jquery-ui-1.12.1/jquery-ui.css">
    <script src="<%=request.getContextPath()%>/resources/vendor/jquery/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/resources/vendor/jquery-ui-1.12.1/jquery-ui.js"></script>
</div>


<script>
    $(document).ready(function() {
        $( "#from" ).datepicker({
            dateFormat: "yy-mm-dd",
            changeMonth: true,
            numberOfMonths: 1,
            onClose: function( selectedDate ) {
                $( "#to" ).datepicker( "option", "minDate", selectedDate );
            }
        });
        $( "#to" ).datepicker({
            dateFormat: "yy-mm-dd",
            changeMonth: true,
            numberOfMonths: 1,
            onClose: function( selectedDate ) {
                $( "#from" ).datepicker( "option", "maxDate", selectedDate );
            }
        });

    });
</script>

</body>
</html>
