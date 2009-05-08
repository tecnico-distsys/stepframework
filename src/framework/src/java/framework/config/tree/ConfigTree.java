package step.framework.config.tree;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;


/**
 *  Configuration Tree.
 *
 *  C holds the configuration data class.
 *  The configuration node identifier is String-based.
 *
 *  The configuration tree is a hierarchy with
 *  zero or more levels of configuration nodes
 *  where increasingly more specific configurations are stored.
 */
public class ConfigTree<C> implements Serializable {

    //
    // Members
    //
    private String name;
    private ConfigPathParser parser;
    private ConfigNode<C> root;


    //
    // Constructors
    //

    /** Construct an empty configuration tree */
    public ConfigTree(ConfigPathParser parser) {
        initName();
        initParser(parser);
        initRoot();
    }

    /** Construct a configuration tree with a default configuration */
    public ConfigTree(ConfigPathParser parser, C defaultConfig) {
        initName();
        initParser(parser);
        initRoot();
        setDefaultConfig(defaultConfig);
    }

    // Construction helper
    private void initParser(ConfigPathParser parser) {
        if(parser == null)
            throw new IllegalArgumentException("parser can't be null");
        this.parser = parser;
    }

    // Construction helper
    private void initRoot() {
        root = new ConfigNode<C>();
    }

    // Construction helper
    private void initName() {
        name = "";
    }


    //
    // Output methods
    //

    /**
     * Returns a brief description of this configuration node.
     * Format is subject to change.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.getClass().getSimpleName());
        sb.append(": ");
        sb.append("name=");
        sb.append(name);
        sb.append(", ");
        sb.append("root=");
        sb.append(root);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Print all contents of configuration tree to System.out stream.
     */
    public void print() {
        print(System.out);
    }

    /**
     * Print all contents of configuration tree to the given print stream.
     */
    public void print(PrintStream out) {
        if(out == null) {
            throw new IllegalArgumentException("print stream can't be null");
        }
        out.println(printToString());
    }

    /**
     * Print all contents of configuration tree to a string.
     */
    public String printToString() {
        final String EOL = System.getProperties().getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        sb.append("Configuration tree");
        if(name.length() > 0) {
            sb.append(" ");
            sb.append(name);
        }
        sb.append(EOL);
        sb.append("root ");
        root.printToString(sb, EOL, 0);

        return sb.toString();
    }


    //
    // Property methods (getters and setters)
    //

    /** Return the configuration tree name. */
    public String getName() {
        return this.name;
    }

    /** Set a new configuration tree name. */
    public void setName(String name) {
        if(name != null)
            this.name = name;
    }

    /** Return the default configuration */
    public C getDefaultConfig() {
        return root.getConfig();
    }

    /**
     *  Set a new default configuration.
     *  The previous default configuration is replaced.
     */
    public void setDefaultConfig(C defaultConfig) {
        root.setConfig(defaultConfig);
    }


    //
    // Configuration path processing methods
    //

    // Helper method to parse path items
    private List<String> parsePathItems(String configPath) {
        if(configPath == null) {
            throw new IllegalArgumentException("config path can't be null");
        }
        List<String> pathItems = parser.parseConfigPath(configPath);
        if(pathItems == null) {
            throw new IllegalArgumentException("config path items can't be null");
        }
        return pathItems;
    }

    /**
     *  Return a stored configuration for given path.
     *  Null is returned if path doesn't exist or if configuration is empty.
     */
    public C getConfig(String configPath) {
        List<String> pathItems = parsePathItems(configPath);
        // pathItems.size() >= 0

        if(pathItems.size() == 0) {
            // return default configuration
            return getDefaultConfig();
        }
        // pathItems.size() >= 1

        ConfigNode<C> currentNode = root;
        Iterator<String> iterator = pathItems.iterator();

        while(iterator.hasNext()) {
            String pathItem = iterator.next();
            ConfigNode<C> node = currentNode.getChildNodes().get(pathItem);
            if(node == null) {
                return null;
            } else {
                currentNode = node;
            }
        }

        // at path config node
        return (C) currentNode.getConfig();
    }

    /**
     *  Store a configuration at given path.
     */
    public void setConfig(String configPath, C config) {
        List<String> pathItems = parsePathItems(configPath);
        // pathItems.size() >= 0

        if(pathItems.size() == 0) {
            // set default configuration
            setDefaultConfig(config);
            return;
        }
        // pathItems.size() >= 1

        ConfigNode<C> currentNode = root;
        Iterator<String> iterator = pathItems.iterator();

        while(iterator.hasNext()) {
            String pathItem = iterator.next();
            ConfigNode<C> node = currentNode.getChildNodes().get(pathItem);
            if(node == null) {
                node = new ConfigNode<C>();
                currentNode.addChildNode(pathItem, node);
            }
            currentNode = node;
        }

        // at path config node
        currentNode.setConfig(config);
        return;
    }

    /**
     *  Return the most specific configuration i.e.
     *  the deepest matching configuration node.
     */
    public C findMostSpecificConfig(String configPath) {
        // parse path
        List<String> pathItems = parsePathItems(configPath);
        // pathItems.size() >= 0

        if(pathItems.size() == 0) {
            // return default configuration
            return getDefaultConfig();
        }
        // pathItems.size() >= 1

        // level 1 path
        if(pathItems.size() == 1) {
            ConfigNode<C> node = root.getChildNodes().get(pathItems.get(0));
            if(node == null) {
                return getDefaultConfig();
            } else {
                C config = node.getConfig();
                return (config == null ? getDefaultConfig() : config);
            }
        }
        // pathItems.size() >= 2

        // find most specific configuration

        ConfigNode<C> node = root;
        C mostSpecificConfig = null;
        Iterator<String> iterator = pathItems.iterator();

        while(true) {

            // fetch configuration
            C config = node.getConfig();
            if(config != null) {
                mostSpecificConfig = config;
            }

            // has more path items?
            if(iterator.hasNext()) {
                // use path item to move downward, if possible
                String pathItem = iterator.next();
                ConfigNode<C> nextNode = node.getChildNodes().get(pathItem);
                if(nextNode == null) {
                    // not possible, return most specific configuration found
                    return mostSpecificConfig;
                } else {
                    // possible, move downward
                    node = nextNode;
                }
            } else {
                // no more path items, return most specific configuration found
                return mostSpecificConfig;
            }

        }

    }

}
