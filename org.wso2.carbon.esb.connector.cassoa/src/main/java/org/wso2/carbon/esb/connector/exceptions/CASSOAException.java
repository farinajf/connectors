package org.wso2.carbon.esb.connector.exceptions;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.wso2.carbon.connector.core.ConnectException;

public class CASSOAException extends ConnectException {
    public static final long serialVersionUID = 1L;

    /**
     *
     * @param messageContext
     * @param code
     * @param message
     * @param detail
     */
    public CASSOAException(final MessageContext messageContext, final String code, final String message, final String detail) {
        super(message);

        messageContext.setProperty(SynapseConstants.ERROR_CODE,    code);
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, message);
        messageContext.setProperty(SynapseConstants.ERROR_DETAIL,  detail);
    }

    /**
     *
     * @param th
     * @param messageContext
     * @param code
     * @param message
     * @param detail
     */
    public CASSOAException(final Throwable th, final MessageContext messageContext, final String code, final String message, final String detail) {
        super(th, message);

        messageContext.setProperty(SynapseConstants.ERROR_CODE,    code);
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, message);
        messageContext.setProperty(SynapseConstants.ERROR_DETAIL,  detail);
    }
}
