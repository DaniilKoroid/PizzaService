<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Order form</title>
</head>
<body>
	
	
	<spring:url value="/app/orders/create" var="createOrderUrl" />
	<form:form method="POST" action="${createOrderUrl}" modelAttribute="orderForm" >
		<form:select path="customer" id="customers" items="${customers}" itemValue="id" />
		<br/>
		<form:select path="deliveryAddress" id="addresses" items="${addresses}" itemValue="id" />
		<br/>
		<table border="3">
			<thead>
				<tr>
					<th>id</th>
					<th>name</th>
					<th>price</th>
					<th>type</th>
					<th>number to order</th>
				</tr>
			</thead>
				<c:forEach items="${pizzas}" var="pizza">
					<tr>
						<td>${pizza.id}</td>
						<td>${pizza.name}</td>
						<td>${pizza.price}</td>
						<td>${pizza.type}</td>
						<td><input name="pizzas[${pizza.id}]" value="0" /></td>
					</tr>
				</c:forEach>
		</table>
		<input type="submit" value="make order"/>
	</form:form>
	
</body>
</html>