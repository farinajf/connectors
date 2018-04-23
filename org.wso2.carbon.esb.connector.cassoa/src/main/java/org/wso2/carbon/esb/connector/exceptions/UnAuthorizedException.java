package org.wso2.carbon.esb.connector.exceptions;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;

public class UnAuthorizedException extends CASSOAException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Unauthorized";
	private static final String CODE    = "401";

	public UnAuthorizedException(final MessageContext messageContext, final String detail) {
		super(detail);
		
		messageContext.setProperty(SynapseConstants.ERROR_CODE,    CODE);
		messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, MESSAGE);
		messageContext.setProperty(SynapseConstants.ERROR_DETAIL,  detail);
	}
	
	public UnAuthorizedException(final MessageContext messageContext, final String detail, final Throwable th) {
		super(th, detail);
		
		messageContext.setProperty(SynapseConstants.ERROR_CODE,    CODE);
		messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, MESSAGE);
		messageContext.setProperty(SynapseConstants.ERROR_DETAIL,  detail);
	}
}
