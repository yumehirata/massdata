<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>めるかり</title>
 <jsp:include page="header.jsp" flush="true" />

</head>
<body>

  <div id="main" class="container-fluid">
    <!-- addItem link -->
    <div id="addItemButton">
      <a class="btn btn-default toDetail" href="${pageContext.request.contextPath}/item/toRegister"><i class="fa fa-plus-square-o"></i> Add New Item</a>
    </div>

    <!-- 検索フォーム -->
    <div id="forms">
      <form action="${pageContext.request.contextPath}/item/search" class="form-inline" role="form">
        <div class="form-group">
          <input type="text" name="name" class="form-control" id="name" placeholder="item name"/>
        </div>
        <div class="form-group"><i class="fa fa-plus"></i></div>
        <div class="form-group">
          <select id="largeCategory" name="largeCategory" class="form-control">
            <option value="">- largeCategory -</option>
            <c:forEach var="largeCategory" items="${largeCategoryList}">
	            <option value="${largeCategory.id}" /><c:out value="${largeCategory.name}"/>
            </c:forEach>
          </select>
          <select id="middleCategory" name="middleCategory" class="form-control">
            <option value="">- middleCategory -</option>
          </select>
          <select id="smallCategory" name="smallCategory" class="form-control">
            <option value="">- smallCategory -</option>
          </select>
        </div>
        <div class="form-group"><i class="fa fa-plus"></i></div>
        <div class="form-group">
          <input type="text" name="brand" class="form-control" placeholder="brand"/>
        </div>
        <div class="form-group"></div>
        <button type="submit" class="btn btn-default"><i class="fa fa-angle-double-right"></i> search</button>
      </form>
    </div>
    
    
    <h4 class="notFoundMessage" style="color:maroon">
	    <c:if test="${message!=null}"><c:out value="${message}"/></c:if>
    </h4>

    <!-- pagination -->
    
    <div class="pages">
      <nav class="page-nav">
        <ul class="pager">
          <li class="previous">
          	<c:if test="${pageNumber!=1}">
	          <span class="fromList">
	 		    <a href="${pageContext.request.contextPath}/item/list?pageNumber=<c:out value="${pageNumber-1}"/>">←prev</a>
			  </span>
			  <span class="fromSearch">
			    <a href="${pageContext.request.contextPath}/item/search?name=<c:out value="${name}"/>&&largeCategory=<c:out value="${largeCategory}"/>&&middleCategory=<c:out value="${middleCategory}"/>&&smallCategory=<c:out value="${smallCategory}"/>&&brand=<c:out value="${brand}"/>&&pageNumber=<c:out value="${pageNumber-1}"/>">←prev</a>
			  </span>
			</c:if>
		  </li>
          <li class="next">
          	<c:if test="${pageLimit!=pageNumber}">
	          <span class="fromList">
 			    <a href="${pageContext.request.contextPath}/item/list?pageNumber=<c:out value="${pageNumber+1}"/>">next→</a>
			  </span>
			  <span class="fromSearch">
			  	<a href="${pageContext.request.contextPath}/item/search?name=<c:out value="${name}"/>&&largeCategory=<c:out value="${largeCategory}"/>&&middleCategory=<c:out value="${middleCategory}"/>&&smallCategory=<c:out value="${smallCategory}"/>&&brand=<c:out value="${brand}"/>&&pageNumber=<c:out value="${pageNumber+1}"/>">next→</a>
			  </span>
			</c:if>
		  </li>
        </ul>
      </nav>
    </div>

    <!-- table -->
    <div class="table-responsive">
      <table id="item-table" class="table table-hover table-condensed">
        <thead>
          <tr>
            <th>name</th>
            <th>price</th>
            <th>category</th>
            <th>brand</th>
            <th>cond</th>
          </tr>
        </thead>
        <tbody>
	 	  <c:forEach var="item" items="${itemList}">
			<tr>
			  <td>
			  	<a class="toDetail" href="${pageContext.request.contextPath}/item/detail?id=<c:out value="${item.id}"/>"><c:out value="${item.name}"/></a>
		   	  </td>
			  <td>
				<c:out value="${item.price}"/>
			  </td>
			  <td>
				<a href="${pageContext.request.contextPath}/item/search?largeCategory=<c:out value="${item.largeCategory.id}"/>"><c:out value="${item.largeCategory.name}"/></a>
				<c:if test="${item.largeCategory.name!=null}"><c:out value=" / "/></c:if>
				<a href="${pageContext.request.contextPath}/item/search?middleCategory=<c:out value="${item.middleCategory.id}"/>"><c:out value="${item.middleCategory.name}"/></a>
				<c:if test="${item.largeCategory.name!=null}"><c:out value=" / "/></c:if>
				<a href="${pageContext.request.contextPath}/item/search?smallCategory=<c:out value="${item.smallCategory.id}"/>"><c:out value="${item.smallCategory.name}"/></a>
			 </td>
			 <td>
				<a href="${pageContext.request.contextPath}/item/search?brand=<c:out value="${item.brand}"/>&&isBrandSearch=true"><c:out value="${item.brand}"/></a>
			 </td>
			 <td>
				<c:out value="${item.condition}"/>
			 </td>
			<tr>
	 	  </c:forEach>
        </tbody>
      </table>
    </div>

    <!-- ダウンロード -->
    <div id="downloadListButton">
      <a class="btn btn-default toDetail" id="downloadButton" href="${pageContext.request.contextPath}/dl/itemlist?pageNumber=<c:out value="${pageNumber}"/>"><i class="fa fa-download" aria-hidden="true"></i> Download This List </a>
    </div>

    <!-- pagination -->
    <div class="pages">
      <nav class="page-nav">
        <ul class="pager">
          <li class="previous">
          	<c:if test="${pageNumber!=1}">
	          <span class="fromList">
	 		    <a href="${pageContext.request.contextPath}/item/list?pageNumber=<c:out value="${pageNumber-1}"/>">←prev</a>
			  </span>
			  <span class="fromSearch">
			    <a href="${pageContext.request.contextPath}/item/search?name=<c:out value="${name}"/>&&largeCategory=<c:out value="${largeCategory}"/>&&middleCategory=<c:out value="${middleCategory}"/>&&smallCategory=<c:out value="${smallCategory}"/>&&brand=<c:out value="${brand}"/>&&pageNumber=<c:out value="${pageNumber-1}"/>">←prev</a>
			  </span>
			</c:if>
		  </li>
          <li class="next">
          	<c:if test="${pageLimit!=pageNumber}">
	          <span class="fromList">
 			    <a href="${pageContext.request.contextPath}/item/list?pageNumber=<c:out value="${pageNumber+1}"/>">next→</a>
			  </span>
			  <span class="fromSearch">
			  	<a href="${pageContext.request.contextPath}/item/search?name=<c:out value="${name}"/>&&largeCategory=<c:out value="${largeCategory}"/>&&middleCategory=<c:out value="${middleCategory}"/>&&smallCategory=<c:out value="${smallCategory}"/>&&brand=<c:out value="${brand}"/>&&pageNumber=<c:out value="${pageNumber+1}"/>">next→</a>
			  </span>
			</c:if>
		  </li>
        </ul>
      </nav>
      
      
      <!-- ページ番号を指定して表示するフォーム -->
      <div id="select-page">

        <form action="${pageContext.request.contextPath}/item/<c:if test="${pageLimit>=49418}">list</c:if><c:if test="${pageLimit<49418}">search</c:if>" class="form-inline" id="fromSearch">
          <div class="form-group">
            <div class="input-group col-xs-6">
              <label></label>
              <input type="text" name="pageNumber" class="form-control"/>
              <c:if test="${pageLimit!=49418}">
              	<input type="hidden" name="name" value="${name}">
              	<input type="hidden" name="largeCategory" value="${largeCategory}">
              	<input type="hidden" name="middleCategory" value="${middleCategory}">
              	<input type="hidden" name="smallCategory" value="${smallCategory}">
              	<input type="hidden" name="brand" value="${brand}">
              </c:if>
              <!-- 総ページ数 -->
              <div class="input-group-addon"><c:out value="${pageLimit}"/></div>
            </div>
            <div class="input-group col-xs-1">
              <button type="submit" class="btn btn-default">Go</button>
            </div>
          </div>
        </form>
      
      </div>
    </div>
    

    
  </div>


</body>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
 <script src="/js/categorySelect.js"></script>
 <script src="/js/checkMessage.js"></script>
 <script src="/js/paging.js"></script>
  <script src="/js/backPrePage.js"></script>
</html>