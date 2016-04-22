package ua.rd.pizzaservice.domain.address;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StateConvertor implements AttributeConverter<State, String>{

	@Override
	public String convertToDatabaseColumn(State attribute) {
		return attribute == null ? null : attribute.state;
	}

	@Override
	public State convertToEntityAttribute(String dbData) {
		return new State(dbData);
	}

	
}
