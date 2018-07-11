<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">

<head>
    <title>Avenger</title>
    <jsp:include page="header.jsp"/>
</head>

<body class="bg-dark">
<div class="container">
    <div class="card card-login mx-auto mt-5">
        <div class="card-header">Login</div>
        <div class="card-body">

            <c:url value="/performLogin" var="loginUrl"/>
            <form:form action="${loginUrl}" method="post">

                <c:if test="${param.error != null}">
                    <div class="alert alert-danger">
                        Invalid username and/or password
                    </div>
                </c:if>

                <div class="form-group">
                    <label for="username">Username</label>
                    <input class="form-control" id="username" name="username" type="text" aria-describedby="emailHelp"
                           value="${param.username}">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input class="form-control" id="password" name="password" type="password">
                </div>
                <input type="submit" class="btn btn-success" value="Login">
            </form:form>
        </div>
    </div>
</div>
</body>

</html>
