<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <jsp:include page="header.jsp" flush="true" />
</head>
<body>

  <!-- login form -->
  <div id="login" class="container">
    <div class="panel panel-default">
      <div class="panel-heading">Login</div>
      <div class="panel-body">
      <span style="color:red"><c:out value="${loginError}"/></span>
        <form action="${pageContext.request.contextPath}/user/login" method="POST">
          <div class="form-group">
            <label for="mail">mail address</label>
            <input type="email" name="name" class="form-control" id="mail" required="required"/>
          </div>
          <div class="form-group">
            <label for="password">password</label>
            <input type="password" name="password" class="form-control" id="password" required="required">
          </div>
          <button type="submit" class="btn btn-default">Login</button>
        </form>
      </div>
    </div>
    <div>
      <a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/user/toRegister"><i class="fa fa-user-plus"></i>&nbsp;Register Account</a>
    </div>
  </div>
</body>
</html>