package ua.rd.pizzaservice.infrastructure.exceptions;

public class NoSuchEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Long entityId;
	
	public NoSuchEntityException(String message, Long entityId) {
		super(message);
		this.entityId = entityId;
	}
	
	public Long getEntityId() {
		return this.entityId;
	}

}
