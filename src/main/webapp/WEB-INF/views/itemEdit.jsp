<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>めかるり</title>
 <jsp:include page="header.jsp" flush="true" />
</head>
<body>

  <!-- details -->
  <div id="input-main" class="container">
    <a type="button" class="btn btn-default" href="${pageContext.request.contextPath}/item/detail?id=<c:out value="${item.id}"/>"><i class="fa fa-reply"></i> back</a>
    <h2>Edit</h2>

	<h4 style="color:maroon"><c:out value="${message}"/></h4>

    <!-- edit form -->
    <form:form action="${pageContext.request.contextPath}/item/edit" modelAttribute="itemForm" method="POST" class="form-horizontal">
        <!-- name -->
        <div class="form-group">
          <label for="inputName" class="col-sm-2 control-label">name</label>
          <div class="col-sm-8">
            <form:input path="name" class="form-control" id="inputName"/>
            <form:errors path="name" cssStyle="color:red" element="div"/>
          </div>
        </div>
        <!-- price -->
        <div class="form-group">
          <label for="price" class="col-sm-2 control-label">price</label>
          <div class="col-sm-8">
            <form:input path="price" class="form-control" id="price"/>
            <form:errors path="price" cssStyle="color:red" element="div"/>
          </div>
        </div>
        <!-- category -->
        <div class="form-group">
          <label for="category" class="col-sm-2 control-label">category</label>
          <div class="col-sm-8">
            <select id="largeCategory" class="form-control">
              <option value="">- largeCategory -</option>
              <c:forEach var="largeCategory" items="${largeCategoryList}">
	             <option value="${largeCategory.id}" /><c:out value="${largeCategory.name}"/>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="category" class="col-sm-2 control-label"></label>
          <div class="col-sm-8">
            <select id="middleCategory" class="form-control">
              <option value="">- middleCategory -</option>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="category" class="col-sm-2 control-label"></label>
          <div class="col-sm-8">
            <select id="smallCategory" name="category" class="form-control">
              <option value="">- smallCategory -</option>
            </select>
              <form:errors path="category" cssStyle="color:red" element="div"/>
          </div>
        </div>
        <!-- brand -->
        <div class="form-group">
          <label for="brand" class="col-sm-2 control-label">brand</label>
          <div class="col-sm-8">
            <form:input id="brand" class="form-control" path="brand"/>
            <form:errors path="brand" cssStyle="color:red" element="div"/>
          </div>
        </div>
        <!-- condition -->
        <div class="form-group">
          <label for="condition" class="col-sm-2 control-label">condition</label>
          <div class="col-sm-8">
            <label for="condition1" class="radio-inline">
              <form:radiobutton path="condition" id="condition1" value="1"/> 1
            </label>
            <label for="condition2" class="radio-inline">
              <form:radiobutton path="condition" id="condition2" value="2"/> 2
            </label>
            <label for="condition3" class="radio-inline">
              <form:radiobutton path="condition" id="condition3" value="3"/> 3
            </label>
              <form:errors path="condition" cssStyle="color:red" element="div"/>
          </div>
        </div>
        <!-- description -->
        <div class="form-group">
          <label for="description" class="col-sm-2 control-label">description</label>
          <div class="col-sm-8">
            <form:textarea path="description" id="description" class="form-control" rows="5"></form:textarea>
            <form:errors path="description" cssStyle="color:red" element="div"/>
          </div>
        </div>
        <!-- submit button -->
        <div class="form-group">
          <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Submit</button>
          </div>
        </div>
        <input type="hidden" name="id" value="${item.id}">
        <input type="hidden" name="id" value="${item.shipping}">
      </form:form>
    </div>

</body>
 <script src="/js/categorySelect.js"></script>
</html>