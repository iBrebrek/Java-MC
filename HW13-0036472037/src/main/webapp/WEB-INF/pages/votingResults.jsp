<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<style type="text/css">
		table.rez td {text-align: center;}
	</style>
</head>
<body <jsp:include page="sessionBackground.jsp" />>

	<h1>Voting results</h1>
	<p>Those are results from voting.</p>
	<table border="1" class="rez">
		<thead><tr><th>Band</th><th>Number for votes</th></tr></thead>
		<tbody>
		<c:forEach var="band" items="${sortedBands}">
			<tr><td>${band.name}</td><td>${band.votes}</td></tr>
		</c:forEach>
		
	</tbody>
	</table>
	
	<h2>Graphic view of results</h2>
	<img alt="Pie-chart" src="voting-graph" width="400" height="400" />
	
	<h2>Results in XLS format</h2>
	<p>Results in XLS format are available <a href="voting-xls">here</a></p>

	<h2>Other things</h2>
	<p>Winners song:</p>
	<ul>
	<c:forEach var="band" items="${winners}">
		<li><a href="${band.songLink}" target="_blank">${band.name}</a></li>
	</c:forEach>
	</ul>

</body>
</html>