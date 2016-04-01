package infrastructure;

public class ServiceLocator {

	private final Config config = new JavaConfig();
	
	private static final ServiceLocator instance = new ServiceLocator();
	
	private ServiceLocator() {
	}
	
	public static ServiceLocator getInstance() {
		return instance;
	}
	
	public Object lookup(String bean) {
		Class<?> clazz = config.getImpl(bean);
		if (clazz == null) {
			throw new RuntimeException("Bean not found");
		}
		
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Bean not found");
		}
	}
}
