<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body <jsp:include page="sessionBackground.jsp" />>

<h1>Voting for favorite band:</h1>
<p>Clink on the link to vote for the band.</p>

<ol>
	<c:forEach var="band" items="${bands}">
		<li><a href="voting-vote?id=${band.id}">${band.name}</a></li>
	</c:forEach>
</ol>

</body>
</html>