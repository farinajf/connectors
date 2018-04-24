package org.wso2.carbon.esb.connector.exceptions;

import org.apache.synapse.MessageContext;

public class UnAuthorizedException extends CASSOAException {
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Unauthorized";
    private static final String CODE    = "401";

    /**
     * 
     * @param messageContext
     * @param detail
     */
    public UnAuthorizedException(final MessageContext messageContext, final String detail) {
        super(messageContext, CODE, MESSAGE, detail);
    }

    /**
     *
     * @param th
     * @param messageContext
     * @param detail
     */
    public UnAuthorizedException(final Throwable th, final MessageContext messageContext, final String detail) {
        super(th, messageContext, CODE, MESSAGE, detail);
    }
}
