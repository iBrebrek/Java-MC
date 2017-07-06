<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div align="right" style="border:2px solid blue;padding:10px">
  <div style="float: left"><a href="/blog/servlets/main">HOME</a></div>

  <c:choose>
    <c:when test="${user != null}">
      Hello <b>${user.firstName} ${user.lastName}</b>
	  <form style="display:inline" action="/blog/servlets/logout" method="get">
    	<input type="submit" value="Logout"/>
	  </form>
    </c:when>
    
    <c:otherwise>
      <form style="display:inline" action="/blog/servlets/register" method="get">
    	<input type="submit" value="Register"/>
	  </form>
      <font color="blue"> ~ OR ~ </font>
      <form style="display:inline" id="login" action="/blog/servlets/main" method="get">
		<input type="submit" value="Login" />
	  </form>
    </c:otherwise>
  </c:choose>
  
</div>
<button type="button" name="back" onclick="history.back()">Go Back</button>