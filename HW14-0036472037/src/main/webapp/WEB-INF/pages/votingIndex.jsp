<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Voting</title>
</head>

<h1>${poll.title}</h1>
<p>${poll.message}</p>

<ol>
	<c:forEach var="option" items="${options}">
		<li><a href="voting-vote?pollID=${poll.id}&id=${option.id}">${option.title}</a></li>
	</c:forEach>
</ol>

</body>
</html>