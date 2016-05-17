<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	

<html>
<head>
	<title>Pizzas assortiment</title>
</head>
<body>
	<h2>Pizzas assortiment</h2>
	<br/>
	<spring:url value="/app/pizzas/create" var="createPizzaUrl" />
	<a href="${createPizzaUrl}">Create pizza</a>
	<table border = "3">
		<thead>
			<tr>
				<th>id</th>
				<th>name</th>
				<th>price</th>
				<th>type</th>
				<th colspan="2">Operations</th>
			</tr>
			
		</thead>
		
		<c:forEach items="${pizzas}" var="pizza">
			<tr>
				<td>${pizza.id}</td>
				<td>${pizza.name}</td>
				<td>${pizza.price}</td>
				<td>${pizza.type}</td>
				<td>
					<spring:url value="/app/pizzas/update/${pizza.id}" var="updateUrl" />
					<a href="${updateUrl}">Update</a>
				</td>
				<td>
					<spring:url value="/app/pizzas/delete/${pizza.id}" var="deleteUrl" />
					<a href="${deleteUrl}">Delete</a>
				</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>