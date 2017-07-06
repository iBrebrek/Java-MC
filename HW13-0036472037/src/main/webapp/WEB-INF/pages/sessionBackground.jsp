<%@page session="true" %>

bgcolor="<%
String color = (String)session.getAttribute("pickedBgCol");
if(color == null) {
	color = "white";
}
out.print(color);
%>"