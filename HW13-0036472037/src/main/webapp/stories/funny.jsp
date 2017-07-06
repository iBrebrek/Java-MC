<%@page import="java.awt.Color"%>
<%@page import="java.util.Random"%>
<%@page session="true" %>

<html>
<body bgcolor="<%
String color = (String)session.getAttribute("pickedBgCol");
if(color == null) {
	color = "white";
}
out.print(color);
%>">

<font color="<%
String[] letters = "0123456789ABCDEF".split("");
String hexColor = "#";
Random random = new Random();
for (int i = 0; i < 6; i++ ) {
	hexColor += letters[random.nextInt(16)];
}
out.print(hexColor); %>">

From the news:
An Oak Hill community couple discovered a thief in their home 
after the man told his wife a joke and they heard a laugh upstairs.

</font>

</body>
</html>