var xmlhttp;

if (window.XMLHttpRequest) {
	// code for IE7+, Firefox, Chrome, Opera, Safari
	xmlhttp = new XMLHttpRequest();
} else {
	// code for IE6, IE5
	xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
}

xmlhttp.onreadystatechange = function() {
	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
		var text = xmlhttp.responseText;
		var tags = JSON.parse(text);

		for (var i = 0; i < tags.length; i++) {
			var element = document.createElement("input");
			var tag = htmlEscape(tags[i]);
			var text = document.createTextNode(tag);
			element.appendChild(text);
		    element.setAttribute("type", "button");
		    element.setAttribute("value", tag);
		    element.setAttribute("class", "tag");
		    element.onclick = function() { 
		    	showThumbnails(this.value);
		    };
			document.getElementById("tags").appendChild(element);
		}
	}
}
xmlhttp.open("GET", "servlets/tags?dummy=" + Math.random(), true);
xmlhttp.send();
