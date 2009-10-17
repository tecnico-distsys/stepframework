package calc;

import javax.xml.ws.*;

import step.framework.config.*;
import step.framework.ws.*;
import step.framework.ws.registry.*;

import calc.ws.stubs.*;


/**
 * Calc stub factory
 *
 *
 */
public class CalcStubFactory extends StubFactory<CalcService,CalcPortType> {

    //
    // required factory methods
    //
    public CalcService getService() {
        return new CalcService();
    }

    public CalcPortType getPort() {
        return this.getService().getCalcPort();
    }

    //
    // config-based factory methods
    //
    public CalcPortType getPortUsingConfig() throws StubFactoryException {
        Config config = Config.getInstance();
        String address = config.getInitParameter("calc.ws.endpoint");

        if(address != null)
            return getPort(address);
        else
            return getPort();
    }

    //
    // registry-based factory methods
    //
    public CalcPortType getPortUsingRegistry() throws StubFactoryException {
        try {
            Registry registry = null;
            ClassificationQuery query = null;
            Registration[] registrationArray = null;
            try {
                registry = new Registry("/Registry.properties");
                query = new ClassificationQuery("/ClassificationQuery.properties");

                registry.connect(false); // no authentication is required for querying

                registrationArray = registry.query(query);

                if(registrationArray == null) {
                    throw new StubFactoryException("No web service registrations found in registry server " + registry.getURL());
                } else {
                    // use first result's URL
                    return getPort(registrationArray[0].getServiceBindingAccessURI());
                }
            } finally {
                if(registry != null)
                    registry.disconnect();
            }
        } catch(RegistryException e) {
            throw new StubFactoryException("Error when stub factory tried to access registry", e);
        }
    }

}
