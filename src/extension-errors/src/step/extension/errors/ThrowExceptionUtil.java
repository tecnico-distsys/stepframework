package step.extension.errors;

import java.lang.reflect.Constructor;


/**
 *  Common extension definitions and helper functions
 */
class ThrowExceptionUtil {

    static void throwException(String exceptionClassname, String throwMethodName)
    throws Exception {

        // we assume the exception has a 1 String argument constructor
        Class ec = null;
        Exception exception = null;
        try {
            // search and instantiate the runtime exception class
            ec = Class.forName(exceptionClassname);

            Constructor constructor = ec.getConstructor(java.lang.String.class);
            String exceptionMessage = "Test Exception generated in method " + throwMethodName;
            exception = (Exception) constructor.newInstance(exceptionMessage);
        } catch(Exception e) {
            // catch, among other exceptions: ClassNotFoundException,
            // InstantiationException, IllegalAccessException
            System.out.println("Could not throw " +
                               exceptionClassname  +
                               " in method " +
                               throwMethodName +
                               " because of " +
                               e.getClass().toString() +
                               " with message " +
                               e.getMessage());
            exception = null;
        }

        if(exception != null) {
            System.out.println(ThrowExceptionUtil.class.getSimpleName() +
                               "> throwing an instance of " +
                               exception.getClass().getName() +
                               " in method " +
                               throwMethodName);
            throw exception;
        }

    }

}
