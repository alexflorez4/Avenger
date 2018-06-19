<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <jsp:include page="footer.jsp"/>
        </div>

    </body>
</html>

