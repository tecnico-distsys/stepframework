package step.groovy;

/**
 *  STEP Framework Groovy helper<br />
 *  <br />
 *  This class provides static helper methods for Groovy scripts.
 *
 *  @author Miguel Pardal
 */
class Helper {

    //
    //  Class Loader -----------------------------------------------------------
    //

    /** Default class loader to use
        (due to a Groovy limitation, this variable cannot be called classLoader) */
    static def cLoader = Helper.classLoader.rootLoader;

    /** Add a class location URL to the current class loader */
    static def addURLToClassLoader(url) {
        assert(url != null);
        cLoader.addURL(url);
    }

    /** Add a file to the current class loader */
    static def addFileToClassLoader(filePath) {
        assert(filePath != null);
        def file = new File(filePath);
        assert(file.exists());
        def url = file.toURI().toURL();
        addURLToClassLoader(url);
    }

    /** Add a directory to the current class loader */
    static def addDirToClassLoader(dirPath) {
        addFileToClassLoader(dirPath);
    }

    /** Add all the JAR files in the directory to the class loader, one by one */
    static def addJarDirToClassLoader(jarDirPath) {
        assert(jarDirPath != null);
        def jarDir = new File(jarDirPath);
        assert(jarDir.exists());
        assert(jarDir.isDirectory());

        jarDir.eachFile() { file ->
            if(file.name.endsWith("jar")) {
                addFileToClassLoader(file.path);
            }
        }
    }


    //
    //  Shell ------------------------------------------------------------------
    //

    /** Execute command on current directory */
    static def exec(command) {
        return exec(command, null);
    }

    /** Execute command on specified directory */
    static def exec(dir, command) {
        return exec(dir, command, null);
    }

    /** Execute command on specified directory setting specified environment variables */
    static def exec(dir, command, envVars) {
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
                // set environment variables
                if (envVars != null) {
                    envVars.each { k, v ->
                        env(key: k, value: v)
                    }
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


    //
    //  Config -----------------------------------------------------------------
    //

    /** Build a ConfigObject merging the contents of all provided configuration files */
    static def parseConfig(... filePathArray) {
        assert (filePathArray.length >= 1)

        // build file list
        def fileList = [ ];
        filePathArray.each { filePath ->
            def file = new File(filePath);
            assert (file.exists())
            fileList.add(file);
        }

        // parse each file in sequence
        def config = new ConfigObject();
        fileList.each { file ->
            config.merge(new ConfigSlurper().parse(file.toURL()));
        }

        return config;
    }


    /** Regular expression used to match config string names that
        will be converted to File objects */
    static def configStringToFileRegex = "(?i).*(File|Dir)";

    /** Convert configuration files and directories to File objects,
        and validate their existence */
    static def configStringToFile(config) {
        configStringToFile(config, true);
    }

    /** Convert configuration files and directories to File objects */
    static def configStringToFile(config, validate) {
        config.each { key, value ->
            if (value instanceof Map) {
                // recursion
                configStringToFile(value, validate)
            } else {
                def matcher = (key =~ configStringToFileRegex);
                if (matcher.matches()) {
                    if (!(value instanceof File)) {
                        assert(value instanceof String)
                        // create File from String value
                        config[key] = new File(value);
                    }
                    if (validate) {
                        def file = config[key];
                        assert(file.exists())
                        if ("dir".equalsIgnoreCase(matcher.group(1))) {
                            assert(file.isDirectory())
                        }
                    }
                }
            }
        }
    }

}
