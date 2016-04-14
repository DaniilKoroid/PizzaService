package infrastructure;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

public class BenchmarkProxyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Class<? extends Object> clazz = bean.getClass();
        Object returnResult = determineResult(bean, clazz);
        return returnResult;
    }

    private Object determineResult(Object bean, Class<? extends Object> clazz) {
        Object returnResult;
        if(!hasBenchmarkAnnotatedMethods(clazz)) {
            returnResult = bean;
        } else {
            returnResult = createProxy(clazz, bean);
        }
        return returnResult;
    }

    private boolean hasBenchmarkAnnotatedMethods(Class<?> clazz) {
        boolean result = false;
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Benchmark.class)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private Object createProxy(Class<?> clazz, Object bean) {
        Class<?>[] interfaces = getAllInterfaces(clazz);
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                Object result;
                long start = System.nanoTime();
                result = method.invoke(bean, args);
                long end = System.nanoTime();
                long delta = end - start;
                System.out.println("Method: " + method.getName() + ". Elapsed time: " + delta);
                return result;
            }
        });
        return proxy;
    }

    private Class<?>[] getAllInterfaces(Class<?> clazz) {
        return ClassUtils.getAllInterfacesForClass(clazz);
    }

}
