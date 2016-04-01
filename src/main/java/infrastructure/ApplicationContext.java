package infrastructure;

public interface ApplicationContext {

	Object getBean(String bean) throws Exception;
}
