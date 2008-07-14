package step.framework.extensions;



/**
 *  This class contains generic static methods that are useful
 *  In different parts of the extensions package implementation.
 *
 *  Its visibility scope is package (absence of qualifier)
 *  so that this code can change without breaking any expectations
 *  outside the extensions package.
 */
class ExtensionsUtil {

    //
    //  Null checking helpers
    //

    /**
     *  This method throws an IllegalArgumentException if the object
     *  to check is null.
     */
    static void throwIllegalArgIfNull(Object objectToCheck, String illegalArgumentMessage)
        throws IllegalArgumentException {

        if(objectToCheck == null) {
            throw new IllegalArgumentException(illegalArgumentMessage);
        }

    }

    /**
     *  This method throws an IllegalStateException if the object
     *  to check is null.
     */
    static void throwIllegalStateIfNull(Object objectToCheck, String illegalStateMessage)
        throws IllegalStateException {

        if(objectToCheck == null) {
            throw new IllegalStateException(illegalStateMessage);
        }

    }

}
