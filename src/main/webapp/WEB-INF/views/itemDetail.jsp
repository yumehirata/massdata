<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>めるかり</title>
 <jsp:include page="header.jsp" flush="true" />
</head>
<body>

  <!-- details -->
  <div class="container">
    <a type="button" class="btn btn-default" href="javascript:history.back()"><i class="fa fa-reply"></i> back</a>
    <a type="button" class="btn btn-default backList"><i class="fa fa-reply"></i> back(query)</a>
    <h2>Details</h2>
    <div id="details">
      <table class="table table-hover">
        <tbody>
          <tr>
            <th>ID</th>
            <td><c:out value="${item.id}"/></td>
          </tr>
          <tr>
            <th>name</th>
            <td><c:out value="${item.name}"/></td>
          </tr>
          <tr>
            <th>price</th>
            <td>$<c:out value="${item.price}"/></td>
          </tr>
          <tr>
            <th>category</th>
            <td>
				<c:out value="${item.largeCategory.name}"/>
				<c:if test="${item.largeCategory.name!=null}"><c:out value=" / "/></c:if>
            	<c:out value="${item.middleCategory.name}"/>
				<c:if test="${item.largeCategory.name!=null}"><c:out value=" / "/></c:if>
				<c:out value="${item.smallCategory.name}"/>
            </td>
          </tr>
          <tr>
            <th>brand</th>
            <td><c:out value="${item.brand}"/></td>
          </tr>
          <tr>
            <th>condition</th>
            <td><c:out value="${item.condition}"/></td>
          </tr>
          <tr>
            <th>description</th>
            <td><c:out value="${item.description}"/></td>
          </tr>
        </tbody>
      </table>
      <a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/item/toEdit?id=<c:out value="${item.id}"/>"><i class="fa fa-pencil-square-o"></i>&nbsp;edit</a>
    </div>
  </div>


</body>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
 <script src="/js/backPrePage.js"></script>
</html>