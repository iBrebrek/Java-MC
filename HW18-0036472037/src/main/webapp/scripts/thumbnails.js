/**
 * Show all thumbnails for given tag.
 * Thumbnails are added to html
 * element with id "thumbnails".
 * 
 * @param tag		picture tag.
 */
function showThumbnails(tag) {
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
			document.getElementById("thumbnails").innerHTML = 
				"<span class='info'>Selected tag: <b>"+htmlEscape(tag)+"</b></span><br>";
			var text = xmlhttp.responseText;
			var pictures = JSON.parse(text);

			for (var i = 0; i < pictures.length; i++) {
				var element = document.createElement("img");
			    element.setAttribute("src", htmlEscape(pictures[i].source));
			    element.setAttribute("alt", htmlEscape(pictures[i].name));
			    element.setAttribute("class", "thumbnail");
			    element.onclick = function() { 
			    	getPicture(this.alt);
			    };
				document.getElementById("thumbnails").appendChild(element);
			}
		}
	}
	xmlhttp.open("GET", "servlets/thumbnails?tag="+encodeURIComponent(tag)+"&dummy=" + Math.random(), true);
	xmlhttp.send();
}