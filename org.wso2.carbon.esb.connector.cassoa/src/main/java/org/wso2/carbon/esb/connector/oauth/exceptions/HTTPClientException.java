/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wso2.carbon.esb.connector.oauth.exceptions;

import org.wso2.carbon.esb.connector.oauth.Constantes;

/**
 *
 * @author fran
 */
public class HTTPClientException extends java.lang.Exception {
    private final int    httpStatus;
    private final String httpMessage;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public HTTPClientException(final int x, final String y) {
        super(new StringBuilder().append(x).append("-").append(y).toString());

        httpStatus  = x;
        httpMessage = Constantes.trimToEmpty(y);
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    public int    getHttpStatus()  {return httpStatus;}
    public String getHttpMessage() {return httpMessage;}
}
