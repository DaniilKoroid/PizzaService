<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Order confirmation</title>
</head>
<body>
	<h2>Order info:</h2>
	<table border="3">
		<thead>
			<tr>
				<th>Customer name</th>
			</tr>
		</thead>
			<tr>
				<td>${order.customer.name}</td>
			</tr>
	</table>
	<br/>
	<table border="3">
		<thead>
			<tr>
				<th>Country</th>
				<th>City</th>
				<th>Zip code</th>
				<th>Street</th>
				<th>Building</th>
				<th>Flat number</th>
			</tr>
		</thead>
			<tr>
				<td>${order.address.country}</td>
				<td>${order.address.city}</td>
				<td>${order.address.zipCode}</td>
				<td>${order.address.street}</td>
				<td>${order.address.building}</td>
				<td>${order.address.flatNumber}</td>
			</tr>
	</table>
	<br/>
	<table border="3">
		<thead>
			<tr>
				<th>pizza name</th>
				<th>number to order</th>
			</tr>
		</thead>
			<c:forEach items="${order.pizzas}" var="entry">
				<tr>
					<td>${entry.key.name}</td>
					<td>${entry.value}</td>
				</tr>
			</c:forEach>
	</table>
</body>
</html>