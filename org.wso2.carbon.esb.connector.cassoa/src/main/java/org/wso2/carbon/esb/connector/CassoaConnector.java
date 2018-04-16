/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.esb.connector;

import java.io.IOException;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.esb.connector.oauth.CASSOAException;
import org.wso2.carbon.esb.connector.oauth.ForbiddenException;
import org.wso2.carbon.esb.connector.oauth.JsonParseException;
import org.wso2.carbon.esb.connector.oauth.OAuth2Provider;
import org.wso2.carbon.esb.connector.oauth.Profile;
import org.wso2.carbon.esb.connector.oauth.UnauthorizedException;

/**
 * Sample method implementation.
 */
public class CassoaConnector extends AbstractConnector {
	private static final String TP_AUTHORIZATION = "Authorization";
	private static final String NO_AUTH_HEADER   = "No hay cabecera de autenticacion";
	private static final String NOT_ALLOWED      = "El usuario no pertenece al grupo: ";
	private static final String EMPTY = "";
	
	private final OAuth2Provider oauth2Provider = null;
	
	/**
	 * 
	 * @param messageContext
	 * @return
	 * @throws UnauthorizedException
	 */
	private String getAuthorizationHeader(final MessageContext messageContext) throws UnauthorizedException {
		Object result = null;

		if ((messageContext instanceof Axis2MessageContext) == false)
        {
			log.warn("CassoaConnector: MessageContext is not instance of Axis2MessageContext!!");
			throw new UnauthorizedException(messageContext, NO_AUTH_HEADER);
		}
		
		final org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext).getAxis2MessageContext();
		final Object                                  headers             = axis2MessageContext.getLocalProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

		if ((headers != null) && (headers instanceof java.util.Map))
        {
			result = ((java.util.Map) headers).get(TP_AUTHORIZATION);
		}

		if ((result == null) || (EMPTY.equals(result.toString().trim())))
        {
			log.warn("CassoaConnector: No header authorization!!");
			throw new UnauthorizedException(messageContext, NO_AUTH_HEADER);
		}

		return (result == null) ? EMPTY : result.toString().trim();
	}

	/**
	 * 
	 * @param messageContext
	 * @param allowedRole
	 * @throws CASSOAException
	 */
	private void authorize(final MessageContext messageContext, final String allowedRole) throws CASSOAException {
        final Profile profile;

		//1.- Obtenemos el Token
		final String accessToken = getAuthorizationHeader(messageContext);
		
        //2.- Obtenemos los permisos
        try
        {
            profile = oauth2Provider.getUSerProfile(accessToken);
        }
        catch (JsonParseException e)
        {
			throw new UnauthorizedException(messageContext, e.getMessage(), e);
		}
		catch (IOException e)
        {
			throw new UnauthorizedException(messageContext, e.getMessage(), e);
		}


		//3.- Comprobamos los permisos
        if (profile.hasRole(allowedRole) == false)
        {
            throw new ForbiddenException(messageContext, new StringBuilder(NOT_ALLOWED).append(allowedRole).toString());
        }
		
		//3.- Todo OK
		return;
	}

	/**
	 * 
	 */
	@Override
	public void connect(MessageContext messageContext) throws ConnectException {
		Object allowedRole = getParameter(messageContext, "allowedRole");
		Object endpoint    = getParameter(messageContext, "endpoint");
		try {
			log.info("allowedRole :" + allowedRole);
			log.info("endpoint    :" + endpoint);
			/**
			 * Add your connector code here
			 **/
		} catch (Exception e) {
			throw new ConnectException(e);
		}
	}
}
