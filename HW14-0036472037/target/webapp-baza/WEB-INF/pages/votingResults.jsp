<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Results</title>
	<style type="text/css">
		table.rez td {text-align: center;}
	</style>
</head>

	<h1>Voting results</h1>
	<p>Those are results from voting.</p>
	<table border="1" class="rez">
		<thead><tr><th>Poll option</th><th>Number for votes</th></tr></thead>
		<tbody>
		<c:forEach var="option" items="${sorted}">
			<tr><td>${option.title}</td><td>${option.votesCount}</td></tr>
		</c:forEach>
		
	</tbody>
	</table>
	
	<h2>Graphic view of results</h2>
	<img alt="Pie-chart" src="voting-graph?pollID=${poll.id}"/>
	
	<h2>Results in XLS format</h2>
	<p>Results in XLS format are available <a href="voting-xls?pollID=${poll.id}">here</a></p>

	<h2>Other things</h2>
	<p>Links to winners:</p>
	<ul>
	<c:forEach var="winner" items="${winners}">
		<li><a href="${winner.link}" target="_blank">${winner.title}</a></li>
	</c:forEach>
	</ul>

</body>
</html>