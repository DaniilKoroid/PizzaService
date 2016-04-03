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
		public Boolean canChange() {
			return Boolean.TRUE;
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
		public Boolean canChange() {
			return Boolean.FALSE;
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
		public Boolean canChange() {
			return Boolean.FALSE;
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
		public Boolean canChange() {
			return Boolean.FALSE;
		}
	},
	;
	
	public abstract Boolean nextState(Order order);
	public abstract Boolean cancel(Order order);
	public abstract Boolean canChange();
}
