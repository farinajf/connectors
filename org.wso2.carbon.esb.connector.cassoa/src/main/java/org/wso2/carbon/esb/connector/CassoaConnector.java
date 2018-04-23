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
import org.wso2.carbon.esb.connector.exceptions.CASSOAException;
import org.wso2.carbon.esb.connector.exceptions.ForbiddenException;
import org.wso2.carbon.esb.connector.exceptions.UnAuthorizedException;
import org.wso2.carbon.esb.connector.oauth.Constantes;
import org.wso2.carbon.esb.connector.oauth.OAuth2Service;
import org.wso2.carbon.esb.connector.oauth.OAuth2ServiceImpl;

/**
 * Sample method implementation.
 */
public class CassoaConnector extends AbstractConnector {
    private final OAuth2Service oauth2Service = new OAuth2ServiceImpl();

    /**
     * Devuelve el mapa con las cabeceras HTTP.
     * @param messageContext
     * @return
     */
    private java.util.Map getHeaders(final MessageContext messageContext) {
        final org.apache.axis2.context.MessageContext axis2MessageContext;
        final java.lang.Object                        result;

        if ((messageContext instanceof Axis2MessageContext) == false)
        {
            log.warn("CassoaConnector: MessageContext is not instance of Axis2MessageContext!!");
            return null;
        }

        if ((axis2MessageContext = ((Axis2MessageContext) messageContext).getAxis2MessageContext()) == null)
        {
            log.warn("CassoaConnector: axis2MessageContext is null!!");
            return null;
        }

        if ((result = axis2MessageContext.getLocalProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS)) == null)
        {
            log.warn("CassoaConnector: TRANSPORT_HEADRES are null!!");
            return null;
        }

        if ((result instanceof java.util.Map) == false)
        {
            log.warn("CassoaConnector: TRANSPORT_HEADRES are not a Map!!");
            return null;
        }

        return (java.util.Map) result;
    }

    /**
     * Devuelve el valor de la cabecera HTTP Authorization
     * @param messageContext
     * @return
     * @throws UnauthorizedException
     */
    private String getAuthorizationHeader(final MessageContext messageContext) throws UnAuthorizedException {
        final java.util.Map headers;
        final Object authHeader;

        if ((headers = getHeaders(messageContext)) == null)
        {
            throw new UnAuthorizedException(messageContext, Constantes.ERROR_NO_AUTH_HEADER);
        }

        if ((authHeader = headers.get(Constantes.TP_AUTHORIZATION)) == null)
        {
            log.warn("CassoaConnector: No authorization header!!");
            throw new UnAuthorizedException(messageContext, Constantes.ERROR_NO_AUTH_HEADER);
        }

        final String result = authHeader.toString().trim();

        if (Constantes.EMPTY.equals(result))
        {
            log.warn("CassoaConnector: authorization header is empty!!");
            throw new UnAuthorizedException(messageContext, Constantes.ERROR_NO_AUTH_HEADER);
        }

        return result;
    }

    /**
     *
     * @param messageContext
     * @param allowedRole
     * @throws CASSOAException
     */
    private void authorize(final MessageContext messageContext, final String endpoint, final String allowedRole) throws CASSOAException {
        final String  response;

        //1.- Obtenemos el Token
        final String accessToken = getAuthorizationHeader(messageContext);

        //2.- Se valida el Token
        try
        {
            if ((response = oauth2Service.getUSerProfile(endpoint, accessToken)) == null)
            {
                log.warn("CassoaConnector: Token no valido!!");
                throw new UnAuthorizedException(messageContext, Constantes.ERROR_TOKEN_NOT_VALID);
            }
        }
        catch (IOException e)
        {
            throw new UnAuthorizedException(messageContext, e.getMessage());
        }

        //3.- Control de acceso
        if (oauth2Service.hasRole(response, allowedRole) == false)
        {
            log.warn("CassoaConnector: no pertenece al grupo (" + allowedRole + ")!!");
            throw new ForbiddenException(messageContext, new StringBuilder(Constantes.ERROR_NOT_ALLOWED).append(allowedRole).toString());
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
