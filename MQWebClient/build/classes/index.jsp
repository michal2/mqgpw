<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="ISO-8859-2"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-2">
<title>WebProject</title>
</head>
<body>

	<h1>Dzień dobry !</h1>
	
	
	<h3>Element z kolejki: <%=getServletContext().getAttribute("elem")%></h3>
	
	
	
	
	<form action="main" method="post">
				<%if (getServletContext().getAttribute("BadDataFormat")==null){ %>
					<img src="file:///home/damian/Pobrane/eclipse/chart.png" />
				<%}else{%>
				<h3><%=getServletContext().getAttribute("BadDataFormat")%></h3>
				<%}%>
				<input type="submit" value="refresh" /> 
	</form>
	

	


</body>
</html>