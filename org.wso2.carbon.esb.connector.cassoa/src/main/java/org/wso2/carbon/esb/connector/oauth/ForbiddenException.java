package org.wso2.carbon.esb.connector.oauth;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;

public class ForbiddenException extends CASSOAException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Forbidden";
	private static final String CODE    = "403";

	public ForbiddenException(final MessageContext messageContext, final String detail) {
		super(MESSAGE);
		
		messageContext.setProperty(SynapseConstants.ERROR_CODE,    CODE);
		messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, MESSAGE);
		messageContext.setProperty(SynapseConstants.ERROR_DETAIL,  detail);
	}
}
