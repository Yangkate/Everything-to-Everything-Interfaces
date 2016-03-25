<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>create</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>

</head>
<body>
<script>
function loadDoc() {
  var xhttp;
  if (window.XMLHttpRequest) {
    // code for modern browsers
    xhttp = new XMLHttpRequest();
    } else {
    // code for IE6, IE5
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
      document.getElementById("show").innerHTML = xhttp.responseText;
    // $("#showbooking").append(xmlhttp.responseText);
    }
  };
  xhttp.open("POST", "HelloWorldServlet", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send("booker="+$('#booker').val()+"&people="+$('#people').val()+"&room="+$('#room').val()+"&distanceToLake="+$('#distanceToLake').val()+"&cityNearBy="+$('#cityNearBy').val()+"&daysForStay="+$('#daysForStay').val()+"&startDate="+$('#startDate').val()+"&maxShift="+$('#maxShift').val());
}
</script>
	

		booker<input type="text" name="booker" id="booker"><br>
		 people<input type="text" name="people" id="people"><br> 
		 room<input type="text" name="room" id="room"><br> 
		 distanceToLake<input type="text" name="distanceToLake" id="distanceToLake"><br> 
		 cityNearBy<input type="text" name="cityNearBy" id="cityNearBy" ><br> 
		 daysForStay<input type="text" name="daysForStay" id="daysForStay"><br> 
		 startDate<input type="text" name="startDate" id="startDate"><br>
		  maxShift<input type="text" name="maxShift" id="maxShift"><br> 
		 <button type="button" onclick="loadDoc()">submit</button>
	
<div id="show">reslut</div>

</body>
</html>
