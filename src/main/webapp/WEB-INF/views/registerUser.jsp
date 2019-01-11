<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <jsp:include page="header.jsp" flush="true" />
</head>
<body>
 
  <!-- register form -->
  <div id="login" class="container">
    <div class="panel panel-default">
      <div class="panel-heading">Register Account</div>
      <div class="panel-body">
        <form:form action="${pageContext.request.contextPath}/user/register" modelAttribute="userForm" method="POST">
          <div class="form-group">
            <label for="mail">mail address</label>
            <form:errors path="name" cssStyle="color:red" element="div"/>
            <input type="email" name="name" class="form-control" id="mail" required="required">
          </div>
          <div class="form-group">
            <label for="password">password</label>
            <form:errors path="password" cssStyle="color:red" element="div"/>
            <form:input path="password" class="form-control" id="password" required="required"/>
          </div>
          <button type="submit" class="btn btn-default">Submit</button>
        </form:form>
      </div>
    </div>
   <a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/user/toLogin"><i class="fa fa-reply"></i>&nbsp;Login page</a>
  </div>

</body>
</html>