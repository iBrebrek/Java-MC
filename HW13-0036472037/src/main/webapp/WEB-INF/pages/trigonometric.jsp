<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="math" class="hr.fer.zemris.java.webapp.Trigonometrics" scope="request" />

<html>
<body <jsp:include page="sessionBackground.jsp" />>

<form action="trigonometric">
	First: <input type="text" name="start"><br>
	Last: <input type="text" name="end"><br>
	<input type="submit"> 
</form>

<table border="1">
	<tr><th>x</th><th>sin(x)</th><th>cos(x)</th></tr>
	<c:forEach var="steps" begin="0" end="${tableSize}">
	<jsp:setProperty property="angle" name="math" value="${steps+startingAngle}" />
		<tr><td>${math.asInt}°</td><td>${math.sin}</td><td>${math.cos}</td></tr>
	</c:forEach>
</table>

</body>
</html>