package step.framework.config.tree.dot;

import java.util.*;

import step.framework.config.tree.*;


public class DotPathParser implements ConfigPathParser {
	private static final long serialVersionUID = 1L;

	public List<String> parseConfigPath(String configPath) throws IllegalArgumentException {

        // empty string should be stored as default configuration
        if(configPath.length() == 0) {
            return new ArrayList<String>(0);
        }
        String[] result = configPath.split("\\.");
        if(result == null || result.length == 0)
            return null;
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < result.length; i++) {
            if(result[i].length() == 0)
                return null;
            list.add(result[i]);
        }
        return list;
    }

}