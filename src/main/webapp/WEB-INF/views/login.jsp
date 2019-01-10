<%@ page language="java" contentType="text/html; charset=UTF-8" %>
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
        <form action="#" method="POST">
          <div class="form-group">
            <label for="mail">mail address</label>
            <input type="email" class="form-control" id="mail">
          </div>
          <div class="form-group">
            <label for="password">password</label>
            <input type="text" class="form-control" id="password">
          </div>
          <button type="submit" class="btn btn-default">Submit</button>
        </form>
      </div>
    </div>
    <div>
      <a type="button" class="btn btn-default" href="./login.html"><i class="fa fa-reply"></i>&nbsp;Login page</a>
    </div>


</body>
</html>