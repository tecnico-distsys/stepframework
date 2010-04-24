package step.framework.wsconfig;

import javax.xml.ws.handler.soap.SOAPMessageContext;

public interface WSConfigurator {
	
	public void configClientOutbound(SOAPMessageContext smc) throws WSConfigurationException;
	public void configServerInbound(SOAPMessageContext smc) throws WSConfigurationException;
	public void configServerOutbound(SOAPMessageContext smc) throws WSConfigurationException;
	public void configClientInbound(SOAPMessageContext smc) throws WSConfigurationException;
}
