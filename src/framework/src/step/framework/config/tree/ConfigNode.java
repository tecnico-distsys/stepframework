package step.framework.config.tree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *  Configuration Node.
 *  Contains a configuration and a map of child configuration nodes.
 */
class ConfigNode<C> implements Serializable {

    //
    // Members
    //
    private C config;
    private Map<String,ConfigNode<C>> childNodes;


    //
    // Constructors
    //

    /** Construct an empty configuration node */
    ConfigNode() {
        initMap();
    }

    /** Construct a configuration node with given configuration. */
    ConfigNode(C config) {
        initMap();
        this.config = config;
    }

    // Construction helper
    private void initMap() {
        childNodes = new HashMap<String,ConfigNode<C>>();
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
        sb.append("config=");
        sb.append(config);
        sb.append(", ");
        sb.append("#childNodes=");
        sb.append(childNodes.size());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Print contents of configuration node to given print stream.
     */
    void printToString(StringBuilder sb, String lineTerminator, int level) {
        final int SPACES_PER_TAB = 4;
        String prefix = spaces(level*SPACES_PER_TAB);

        sb.append(this.toString());
        sb.append(lineTerminator);

        for(String key : this.childNodes.keySet()) {
            sb.append(prefix);
            sb.append(key);
            sb.append(" ");
            ConfigNode<C> node = this.childNodes.get(key);
            node.printToString(sb, lineTerminator, level+1);
        }
    }

    /*
     * spaces - credit to Tom Hawtin (http://jroller.com/page/tackline/)
     *
     * String.substring shares the underlying char array.
     * Requires Java 5.0 in order to be thread-safe.
     */
    private final static String SPACES = "                                ";

    private static String spaces(final int len) {
        if (len < 0) {
            throw new IllegalArgumentException();
        } else {
            if(len > SPACES.length()) {
                throw new IllegalArgumentException("Maximum spaces allowed are " + SPACES.length());
            }
        }
        return SPACES.substring(0, len);
    }


    //
    // Property methods (getters and setters)
    //

    /** get node configuration */
    C getConfig() {
        return this.config;
    }

    /** set node configuration */
    void setConfig(C config) {
        this.config = config;
    }

    /** get child node map */
    Map<String,ConfigNode<C>> getChildNodes() {
        return this.childNodes;
    }

    /** add child node to map */
    void addChildNode(String configPathItem, ConfigNode<C> child) {
        checkArgNotNull(configPathItem);
        checkArgNotNull(child);
        this.childNodes.put(configPathItem, child);
    }

    // Helper method to check if argument is not null
    private void checkArgNotNull(Object obj) {
        if(obj == null)
            throw new IllegalArgumentException();
    }

}