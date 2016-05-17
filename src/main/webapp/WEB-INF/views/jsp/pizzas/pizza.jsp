<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Pizza creating/updating form</title>
</head>

<body>
	<h2>
		<c:choose>
			<c:when test="${not empty create}">Creating pizza</c:when>
			<c:otherwise>Updating pizza</c:otherwise>
		</c:choose>
	</h2>
	<spring:url value="/app/pizzas" var="pizzasUrl" />
	<form:form action="${pizzasUrl}" method="POST" modelAttribute="pizzaForm">
		<form:hidden path="id" />
		<table>
			<tr>
				<td><form:label path="name">Pizza name</form:label></td>
				<td><form:input path="name" id="name" placeholder="Pizza name" /></td>
			</tr>
			<tr>
				<td><form:label path="price">Pizza price</form:label></td>
				<td><form:input path="price" id="price" placeholder="Pizza price" /></td>
			</tr>
			<tr>
				<td><form:label path="type">Choose pizza type:</form:label></td>
				<td>
					<form:select path="type" placeholder="Choose pizza type:">
						<form:options items="${pizzaTypes}" />
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
					<input type="submit" />		
				</td>
			</tr>
		</table>
	</form:form>
</body>
</html>