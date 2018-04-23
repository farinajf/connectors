package org.wso2.carbon.esb.connector.
oauth;

import org.wso2.carbon.connector.core.ConnectException;

public class CASSOAException extends ConnectException {
	public static final long serialVersionUID = 1L;
	
	public CASSOAException(final String msj) {
		super(msj);
	}
	
	public CASSOAException(final Throwable th, final String msj) {
		super(th, msj);
	}
}
