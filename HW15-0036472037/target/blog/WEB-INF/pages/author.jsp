<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
  <body>
  
  <jsp:include page="header.jsp"/>
  
  <div align="center">
    <br>
  	<p>All blog entries of user <b>${nick}</b>:</p>
	<c:forEach var="entry" items="${entries}">
	  <a href=" ${nick}/${entry.id}">${entry.title}</a><br>
	</c:forEach>
  	
  	<c:if test="${user.nick == nick}">
  	  <hr>
  	  <a href=" ${nick}/new">Create new blog entry.</a>
  	</c:if>
  </div>
  
  </body>
</html>