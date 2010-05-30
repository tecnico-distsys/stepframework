/**
 *  Config helper.
 */

class ConfigHelper {

    /** Build a ConfigObject merging the contents of all provided configuration files */
    static def parseMany(... filePathArray) {
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

}
