package step.framework.config.tree;

import java.io.Serializable;
import java.util.List;

/**
 *  Configuration Path Parser.
 *
 *  An implementing class must be able to parse a configuration path
 *  into a list of configuration path items.
 */
public interface ConfigPathParser extends Serializable {

    /**
     *  Parses a configuration path into a list of configuration path items.
     *
     *  The items are used to store configuration data in the tree.
     *  Each item corresponds to a tree level.
     *
     *  If the configuration path is invalid the implementation should
     *  throw an IllegalArgumentException or return null. In either case
     *  no configuration data will be stored.
     *
     *  If the implementation returns an empty list then the configuration will
     *  be stored as the default configuration.
     *
     */
    public List<String> parseConfigPath(String configPath)
        throws IllegalArgumentException;

}
