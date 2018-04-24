/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wso2.carbon.esb.connector.oauth;

/**
 *
 * @author fran
 */
public final class Constantes {
    public static final String TP_AUTHORIZATION      = "Authorization";
    public static final String ERROR_NO_AUTH_HEADER  = "No hay cabecera de autenticacion";
    public static final String ERROR_TOKEN_NOT_VALID = "TOKEN no valido";
    public static final String ERROR_NOT_ALLOWED     = "El usuario no pertenece al grupo: ";
    public static final String EMPTY                 = "";

    /**
     *
     * @param x
     * @return
     */
    public static String trimToEmpty(final String x) {
        return (x == null) ? EMPTY : x.trim();
    }

    /**
     *
     * @param x
     * @return
     */
    public static boolean isEmpty(final String x) {
        if (x == null) return true;

        return EMPTY.equals(x.trim());
    }
}
