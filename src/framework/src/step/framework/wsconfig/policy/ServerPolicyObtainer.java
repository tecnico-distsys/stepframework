package step.framework.wsconfig.policy;

import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import step.framework.wsconfig.policy.PolicyUtil.PolicyTag;
import step.framework.wsconfig.wsdl.WSDLDocument;
import step.framework.wsconfig.wsdl.WSDLObtainer;

public class ServerPolicyObtainer {

	private WSDLObtainer wsdlObt;

	public ServerPolicyObtainer(WSDLObtainer wsdlObt)
	{
		this.wsdlObt = wsdlObt;
	}

	public Policy getServicePolicy() throws Exception
	{
		WSDLDocument wsdl = wsdlObt.getWSDL();
		Node node = getNode(wsdl, PolicyTag.Policy, WSDLDocument.NS_WSDL11, WSDLDocument.TAG_SERVICE);
		return PolicyEngine.getPolicy(PolicyUtil.toOM(node));
	}
	
	private Node getNode(WSDLDocument wsdl, PolicyTag tag, String parentNS, String parentName)
	{
		NodeList nodes = PolicyUtil.getPolicyNodes(wsdl, tag);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Node parent = node.getParentNode();
			if (parent.getNamespaceURI().equals(parentNS) && parent.getNodeName().equals(parentName)) {
				return node;
			}
		}
		return null;
	}
}
