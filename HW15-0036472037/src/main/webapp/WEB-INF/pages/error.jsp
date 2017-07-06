<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Error page</title>
	<style>
		span {
    		height: 200px;
    		width: 400px;
    		background: white;
    		color: red;

    		position: fixed;
    		top: 50%;
    		left: 50%;
    		margin-top: -100px;
    		margin-left: -200px;
		}
	</style>
</head>

<body>

  <jsp:include page="header.jsp"/>

  <span>
    <h1>Error...</h1>
    <c:choose>
      <c:when test="${error != null}">
     	<p>${error}</p>
      </c:when>
      <c:otherwise>
    	<p>You probably entered invalid page.</p>
      </c:otherwise>
    </c:choose>
   
   </span>
</body>

</html>