<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <body>
  
  <jsp:include page="header.jsp"/>
  
  <div align="center">
  
    <h1>Registration</h1>
  
    <form id="register" action="register" method="post">
	  <label for="name">First Name: </label><br>
	  <input type="text" name="name" maxlength="25" value="${name}"/><br>
	  <font color="red">${invalidName}</font>
	  <br><br>

	  <label for="last">Last Name: </label><br>
	  <input type="text" name="last" maxlength="25" value="${last}"/><br>
      <font color="red">${invalidLast}</font>
	  <br><br>
	
	  <label for="mail" >Email Address:</label><br>
	  <input type="text" name="mail" maxlength="50" value="${mail}"/><br>
      <font color="red">${invalidMail}</font>
  	  <br><br>
	
	  <label for="nick">Nick: (used to login)</label><br>
	  <input type="text" name="nick" maxlength="25" value="${nick}"/><br>
      <font color="red">${invalidNick}</font>
	  <br><br>
	
	  <label for="pass" >Password:</label><br>
	  <input type="password" name="pass" value="${pass}"/><br>
      <font color="red">${invalidPass}</font>
	  <br><br>
	
	  <label for="passR" >Repeat Password:</label><br>
	  <input type="password" name="passR" value="${passR}"/><br>
      <font color="red">${invalidPassR}</font>
	  <br><br>
	
	  <input type="submit" name="submit" value="Register" />
    </form>
  
  </div>
  
  </body>
</html>