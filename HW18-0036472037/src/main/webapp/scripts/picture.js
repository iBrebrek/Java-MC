/**
 * Loads picture with given name
 * and adds that picture to html
 * element with id "picture"
 * 
 * @param name		picture name.
 */
function getPicture(name) {
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
			var picture = JSON.parse(text);
			
			var html = "<p class='title'>"+htmlEscape(picture.name)+"<br>";
			html += "<p class='info'> Tags of this picture: <b>";
			var tags = picture.tags;
			for (var i = 0; i < tags.length; i++) {
				if(i != 0) {
					html += ", ";
				}
				html += htmlEscape(tags[i]);
			}
			html += "</b></p></p>";
			html += "<img src=\""+htmlEscape(picture.source)+"\"/><br>";
			document.getElementById("picture").innerHTML = html;
		}
	}
	xmlhttp.open("GET", "servlets/picture?name="+encodeURIComponent(name)+"&dummy=" + Math.random(), true);
	xmlhttp.send();
}