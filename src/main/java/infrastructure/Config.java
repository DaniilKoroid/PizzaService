package infrastructure;

public interface Config {
	Class<?> getImpl(String bean);
}
