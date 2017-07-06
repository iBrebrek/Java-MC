<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
  <body>
  
  <jsp:include page="header.jsp"/>
  
  <div align="center">
    <br>
    <b>${blog.title}</b><br>
    
    <i>by: ${blog.creator.nick}</i>
  	<p>${blog.text}</p>
  	<c:if test="${user.nick == nick}">
  	  <a href="${blog.id}/edit">Edit this entry</a>
  	</c:if>
  	<hr>
  	<br><br>
  	Comments:
  	<hr>
	<c:forEach var="comment" items="${blog.comments}">
	  Posted on: ${comment.postedOn}<br>
	  ${comment.message}<hr>
	</c:forEach>
  	
  	Add a comment: <font color="red">${invalidMessage}</font>
  	<form method="post">
  	  <input type="hidden" name="eid" value="${blog.id}"/>
  	  <input type="hidden" name="mail" value="${user.email}"/>
  	  <input type="hidden" name="nick" value="${user.nick}"/>
      <textarea name="message" rows="5" cols="30">${message}</textarea>
      <br>
    <input type="submit" name="submit" value="Comment"/>
  	</form>
  </div>
  
  </body>
</html>