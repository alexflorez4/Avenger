<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


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
      <form:form  action="authenticate.do" method="post">
          <div class="form-group">
            <label for="username">Email address</label>
            <input class="form-control" id="username" name="username" type="text" aria-describedby="emailHelp" placeholder="User name">
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input class="form-control" id="password" name="password" type="password" placeholder="Password">
          </div>
        <input  type="submit" class="btn btn-success" value="Login">
      </form:form>
      </div>
    </div>
  </div>


  <div class="content-wrapper">
    <jsp:include page="footer.jsp"/>
  </div>
</body>

</html>
