/**
 *  Shell helper.
 */

class ShellHelper {

    /** Execute command on current directory */
    static def exec(command) {
        return exec(command, null);
    }

    /** Execute command on specified directory */
    static def exec(dir, command) {
        assert (command)

        def ant = new AntBuilder()
        // disable output adornments i.e. enter emacs mode - thanks to Christoph Metzendorf
        def logger = ant.project.buildListeners.find { it instanceof org.apache.tools.ant.DefaultLogger }
        logger.emacsMode = true

        if (dir == null) dir = "";

        assertOS("windows");

        ant.sequential {
            ant.exec(executable: "cmd", dir: dir as String, resultproperty: "exec-return-code") {
                arg(value: "/c")
                arg(line: command as String)
                if (command ==~ "(?i)java.*" || command ==~ "(?i)ant.*") {
                    // classpath is cleared to avoid conflicts with groovy's classpath definitions
                    env(key: "CLASSPATH", value: "")
                }
            }
        }
        
        def returnCode = ant.project.properties["exec-return-code"]
        assert ((returnCode as Integer) == 0) : "Return code was not zero"
    }

    /** Assert that current operating systems belongs to the specified family */
    private static def assertOS(osFamily) {
        final def PROP_NAME = "is-os";
        final def TRUE = "true";
        final def FALSE = "false";

        def ant = new AntBuilder();

        ant.sequential {
            ant.condition(property: PROP_NAME, value: TRUE, else: FALSE) {
                os(family: osFamily)
            }
        }

        def isOS = ant.project.properties[PROP_NAME];
        assert (TRUE.equals(isOS)): "Operating system family is not " + osFamily + " as expected"
    }

}
