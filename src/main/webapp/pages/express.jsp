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
                <h1>Express Order</h1>
                <hr>
                <!-- Breadcrumbs-->
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">Amazon Orders</li>
                </ol>

                <p>Upload Orders File</p>

                <c:choose>
                    <c:when test="${fn:length(failingOrdersSet) gt 0}">
                        <div class="alert alert-info">
                            The following orders could not be processed:
                            <c:forEach items="${failingOrdersSet}" var="order">
                                <li>Amazon Order Id: ${order.marketOrderId}</li>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:when test="${OrderSuccess}">
                        <div class="alert alert-success">
                            Orders have been submitted successfully!
                        </div>
                    </c:when>
                </c:choose>


                <form class="form-inline" method="POST" action="processExpress.do" enctype="multipart/form-data">
                    <div class="form-group mx-sm-3 mb-2">
                        <input type="file" class="form-control" id="ordersFile" name="ordersFile">
                    </div>
                    <button type="submit" class="btn btn-primary mb-2">Submit</button>
                </form>
            </div>
            <!-- /.container-fluid-->
            <jsp:include page="footer.jsp"/>
        </div>
        <!-- /.content-wrapper-->
    </body>
</html>

