package step.framework.extensions;

import java.util.ArrayList;
import java.util.List;

import step.framework.config.tree.ConfigPathParser;

class WebServiceConfigPathParser implements ConfigPathParser {

    /** Character that starts the namespace declaration */
    public static final String BEGIN_NAMESPACE_SPECIFIER = "{";
    /** Character that ends the namespace declaration */
    public static final String END_NAMESPACE_SPECIFIER = "}";

    /** Length of namespace specifier characters (begin + end) */
    private static final int NAMESPACE_SPECIFIER_LENGTH = BEGIN_NAMESPACE_SPECIFIER.length() +
                                                          END_NAMESPACE_SPECIFIER.length();

    /** A web service config path contains, at most: namespace, service, port */
    public static final int MAX_WEB_SERVICE_CONFIG_PATH_ITEMS = 3;

    /** Character that separates the service, port names in a web service
        configuration path */
    public static final String WEB_SERVICE_CONFIG_PATH_ITEM_SEPARATOR = ".";

    /** Regular expression to split a configuration path items */
    private static final String WEB_SERVICE_CONFIG_PATH_ITEM_SEPARATOR_REG_EXP = "\\.";



    /**
     *  Parse a Web Service name to a list with its elements:
     *  namespace, service name and port name.
     *
     *  Valid config path examples:
     *  {myNamespace}
     *  {myNamespace}MyService
     *  {myNamespace}MyService.MyPort
     */
    public List<String> parseConfigPath(String configPath) throws IllegalArgumentException {
        // check config path
        ExtensionsUtil.throwIllegalArgIfNull(configPath,
                                             "web service config path can't be null");

        // trim leading and trailing whitespace
        configPath = configPath.trim();

        // empty specifier is default config
        if(configPath.length() == 0) {
            // return empty list to store as default config
            return new ArrayList<String>(0);
        }

        // check namespace specifier beginning and end
        final int idxBegin = configPath.indexOf(BEGIN_NAMESPACE_SPECIFIER);
        if(idxBegin == -1) {
            throw new IllegalArgumentException("begin namespace character " +
                                               BEGIN_NAMESPACE_SPECIFIER +
                                               " not found in web service config path " +
                                               configPath);
        }
        final int idxEnd = configPath.indexOf(END_NAMESPACE_SPECIFIER);
        if(idxBegin == -1) {
            throw new IllegalArgumentException("end namespace character " +
                                               END_NAMESPACE_SPECIFIER +
                                               " not found in web service config path " +
                                               configPath);
        }
        if(idxEnd < idxBegin) {
            throw new IllegalArgumentException("begin namespace character " +
                                               BEGIN_NAMESPACE_SPECIFIER +
                                               " must appear before end namespace character " +
                                               END_NAMESPACE_SPECIFIER +
                                               " in web service config path " +
                                               configPath);
        }
        String namespace = configPath.substring(idxBegin+1, idxEnd);

        // trim leading and trailing whitespace
        namespace = namespace.trim();

        // check if namespace is empty
        if(namespace.length() == 0) {
            throw new IllegalArgumentException("namespace can't be empty " +
                                               "in web service config path " +
                                               configPath);
        }

        // create return list
        List<String> resultList = new ArrayList<String>(MAX_WEB_SERVICE_CONFIG_PATH_ITEMS);
        resultList.add(namespace);

        // check if something more as been specified after namespace
        if(configPath.length() > (namespace.length() + NAMESPACE_SPECIFIER_LENGTH)) {
            String rest = configPath.substring(idxEnd+1, configPath.length());
            String[] splitResult = rest.split(WEB_SERVICE_CONFIG_PATH_ITEM_SEPARATOR_REG_EXP);
            // splitResult is never null because rest always has some contents
            for(int i = 0; i < splitResult.length; i++) {
                // trim leading and trailing whitespace
                splitResult[i] = splitResult[i].trim();
                // check if item is empty
                if(splitResult[i].length() == 0) {
                    throw new IllegalArgumentException("web service config path " +
                                                       configPath +
                                                       " can't contain empty items");
                }
                // check if maximum items have been reached
                if(i > MAX_WEB_SERVICE_CONFIG_PATH_ITEMS) {
                    throw new IllegalArgumentException("web service config path " +
                                                       configPath +
                                                       " can contain, at most, " +
                                                       MAX_WEB_SERVICE_CONFIG_PATH_ITEMS +
                                                       " items");
                }
                // item OK, add it to result list
                resultList.add(splitResult[i]);
            }
        }

        return resultList;
    }


    /**
     *  Function that composes a web service interception configuration path.
     */
    public static String composeConfigPath(String namespace,
                                           String service,
                                           String port) {
        ExtensionsUtil.throwIllegalArgIfNull(namespace,
                                            "namespace in config path can't be null");
        ExtensionsUtil.throwIllegalArgIfNull(service,
                                            "service in config path can't be null");
        ExtensionsUtil.throwIllegalArgIfNull(port,
                                            "port in config path can't be null");

        StringBuilder sb = new StringBuilder(namespace.length() +
                                             service.length() +
                                             port.length() +
                                             NAMESPACE_SPECIFIER_LENGTH);
        sb.append(BEGIN_NAMESPACE_SPECIFIER);
        sb.append(namespace);
        sb.append(END_NAMESPACE_SPECIFIER);
        sb.append(service);
        sb.append(WEB_SERVICE_CONFIG_PATH_ITEM_SEPARATOR);
        sb.append(port);
        return sb.toString();
    }

}
