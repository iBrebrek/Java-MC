<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
  <body>
  
  <jsp:include page="header.jsp"/>
  
  <div align="center">
    <br>
  	<h1>Welcome to Blog</h1>
    <c:if test="${user == null}">
      <hr><br><br>
      <form id="login" action="main" method="post">
 		<input type="text" name="user" maxlength="25" placeholder="Nick..." value="${nick}"/>
 		<input type="password" name="password" placeholder="Password..."/>
		<input type="submit" value="Login" />
	  </form>
	  <font color="red">${invalidUser}</font>
	  <font color="red">${invalidPassword}</font>
    </c:if>

  	<br><hr><br>
  	<a href="register">Create new blog user.</a>
  	<br><br><hr><br>
  	<p>All blog users:</p>
	  <c:forEach var="user" items="${users}">
		<a href="author/${user.nick}">${user.nick}</a><br>
	  </c:forEach>
  
  </div>
  
  </body>
</html>