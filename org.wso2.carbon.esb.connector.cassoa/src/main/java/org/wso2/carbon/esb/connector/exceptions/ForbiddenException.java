package org.wso2.carbon.esb.connector.exceptions;

import org.apache.synapse.MessageContext;

public class ForbiddenException extends CASSOAException {
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Forbidden";
    private static final String CODE    = "403";

    /**
     *
     * @param messageContext
     * @param detail
     */
    public ForbiddenException(final MessageContext messageContext, final String detail) {
        super(messageContext, CODE, MESSAGE, detail);
    }

    /**
     *
     * @param messageContext
     * @param detail
     */
    public ForbiddenException(final Throwable th, final MessageContext messageContext, final String detail) {
        super(th, messageContext, CODE, MESSAGE, detail);
    }
}
