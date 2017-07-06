<%@page session="true" %>

<html>
<body <jsp:include page="WEB-INF/pages/sessionBackground.jsp" />>

<p>This web app was started before:</p>
<% 
long start = (Long)getServletContext().getAttribute("milisecStart"); 
long milisec = System.currentTimeMillis() - start;
long sec = milisec / 1000;
long min = sec / 60;
long hour = min / 60;
long days = hour / 24;
milisec -= sec*1000;
sec -= min*60;
min -= hour*60;
hour -= days*24;

String output = "";
if(days != 0) output += days+" days, ";
if(hour != 0) output += hour+" hours, ";
if(min != 0) output += min+" minutes, ";
if(sec != 0) output += sec+" seconds, ";
output += milisec+" milliseconds";

out.print(output);
%>

<br><a href="index.jsp">Back to index</a>

</body>
</html>