package ua.rd.pizzaservice.domain.order;

public enum OrderState {

	NEW {
		@Override
		public Boolean cancel(Order order) {
			order.setState(CANCELLED);
			return Boolean.TRUE;
		}

		@Override
		public Boolean nextState(Order order) {
			order.setState(IN_PROGRESS);
			return Boolean.TRUE;
		}

		@Override
		public Boolean canProceedTo(OrderState state) {
			Boolean result;
			switch (state) {
			case IN_PROGRESS:
			case CANCELLED:
				result = Boolean.TRUE;
				break;
			case NEW:
			case DONE:
			default:
				result = Boolean.FALSE;
			}
			return result;
		}
	},
	IN_PROGRESS {

		@Override
		public Boolean nextState(Order order) {
			order.setState(DONE);
			return Boolean.TRUE;
		}

		@Override
		public Boolean cancel(Order order) {
			order.setState(CANCELLED);
			return Boolean.TRUE;
		}

		@Override
		public Boolean canProceedTo(OrderState state) {
			Boolean result;
			switch (state) {
			case DONE:
			case CANCELLED:
				result = Boolean.TRUE;
				break;
			case NEW:
			case IN_PROGRESS:
			default:
				result = Boolean.FALSE;
			}
			return result;
		}
	},
	DONE {

		@Override
		public Boolean nextState(Order order) {
			return Boolean.FALSE;
		}

		@Override
		public Boolean cancel(Order order) {
			return Boolean.FALSE;
		}

		@Override
		public Boolean canProceedTo(OrderState state) {
			Boolean result = Boolean.FALSE;
			return result;
		}
	},
	CANCELLED {

		@Override
		public Boolean nextState(Order order) {
			return Boolean.FALSE;
		}

		@Override
		public Boolean cancel(Order order) {
			return Boolean.FALSE;
		}

		@Override
		public Boolean canProceedTo(OrderState state) {
			Boolean result = Boolean.FALSE;
			return result;
		}
	}, ;

	public abstract Boolean nextState(Order order);

	public abstract Boolean cancel(Order order);

	public abstract Boolean canProceedTo(OrderState state);
}
