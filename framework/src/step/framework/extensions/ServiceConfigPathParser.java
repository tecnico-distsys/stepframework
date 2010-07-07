package step.framework.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import step.framework.config.tree.ConfigPathParser;

class ServiceConfigPathParser implements ConfigPathParser {

	private static final long serialVersionUID = 1L;

	/**
     *  Parse a Java name to a list with its elements:
     *  package and simple name.
     *
     *  Valid config path examples:
     *  org.app.Class
     *  org.app
     *  org
     *
     */
    public List<String> parseConfigPath(String configPath) throws IllegalArgumentException {
        // check config path
        ExtensionsUtil.throwIllegalArgIfNull(configPath,
                                            "service config path can't be null");

        // trim leading and trailing whitespace
        configPath = configPath.trim();

        if(configPath.length() == 0) {
            // return empty list to store as default config
            return new ArrayList<String>(0);
        }

        // split using separator
        String[] result = configPath.split("\\.");
        if(result == null || result.length == 0) {
            throw new IllegalArgumentException("invalid service config path: '" +
                                               configPath + "'");
        }

        // trim leading and trailing whitespace, check if empty
        for(int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
            if(result[i].length() == 0) {
                throw new IllegalArgumentException("invalid service config path: '" +
                                                   configPath + "'");
            }
        }

        // convert to list
        return Arrays.asList(result);

    }

}