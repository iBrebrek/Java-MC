<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Index</title>
</head>


<h1>Polls</h1>
<p>Select a poll:</p>

<ol>
	<c:forEach var="poll" items="${polls}">
		<li><a href="voting?pollID=${poll.id}">${poll.title}</a></li>
		<br>
	</c:forEach>
</ol>

</body>
</html>