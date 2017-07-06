<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
  <body>
  
  <jsp:include page="header.jsp"/>
  
  <div align="center"><h1>Write a blog entry</h1></div>
  
  <form method="post">
  	<input type="hidden" name="eid" value="${entry.id}">
    Title: <font color="red">${invalidTitle}</font><br>
    <input type="text" name="title" maxlength="25" value="${entry.title}"/><br>
    Text: <font color="red">${invalidText}</font><br>
    <textarea name="text" rows="15" style="min-width: 100%">${entry.text}</textarea>
    <br>
    <input type="submit" name="submit" value="Save"/>
  </form>
  
  </body>
</html>