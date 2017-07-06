<html>
	<head>
		<meta charset="UTF-8">
		<title>Gallery</title>

		<style>
			h1 {
		  		font-style: italic;
		  	}
		  	.tag {
		  		margin-bottom: 3px;
		  		margin-right: 3px; 
		  		color: #444444;
    			width: 180px;
    			height: 25px;
    			padding-left: 10px;
    		}
		  	.thumbnail { 
		  		margin-top: 15px;
		  		margin-bottom: 15px;
		  		margin-left: 15px; 
		  		margin-right: 15px; 
		  		border: 1px solid #FFAAAA; 
		  		vertical-align: middle;
		  	}
		  	.thumbnail:HOVER { 
		  		margin-top: 3px; 
		  		margin-bottom: 3px;
		  		margin-left: 3px;
		  		margin-right: 3px;
		  		width: 170px;
    			height: 170px;
		  		border: 3px solid #FFAAAA; 
		  		vertical-align: middle;
		  	}
		  	.title {
		  		font-size:25px;
		  		background-color: blue; 
		  		color: white; 
		  		font-family: aeriel; 
		  	}
		  	.info {
		  		font-style: italic;
		  		color: grey;
		  	}
		</style>

		<script type="text/javascript" src="scripts/htmlescaping.js"></script>
		<script type="text/javascript" src="scripts/loadtags.js"></script>
		<script type="text/javascript" src="scripts/thumbnails.js"></script>
		<script type="text/javascript" src="scripts/picture.js"></script>
	</head>
	<body>
	
		<div align="center"><h1>Gallery</h1></div>
		
		<p class="info">Tags from all pictures:</p>
		<div id="tags"></div><hr>
		<div id="thumbnails"></div><hr>
		<span class="info">Currently selected picture:</span><br>
		<div align="center" id="picture"><i>Nothing selected</i></div>
	</body>
</html>