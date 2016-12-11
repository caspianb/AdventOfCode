import java.lang.reflect.Method;

public class Application {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Must specify which class to invoke...");
            return;
        }

        String className = args[0];
        Class<?> classType = Application.class.getClassLoader().loadClass("problems." + className);
        Method method = classType.getMethod("main", String[].class);
        method.invoke(null, (Object) args);
    }
}
