/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wso2.carbon.esb.connector.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fran
 */
class HttpClient {
    private final Log    log = LogFactory.getLog(this.getClass());
    private final String endpoint;

    /**
     *
     * @param socket
     * @return
     */
    private String getError(final HttpURLConnection socket) {
        try
        {
            return new StringBuilder("HTTP Status:").append(socket.getResponseCode()).append(" - ").append(socket.getResponseMessage()).toString();
        }
        catch (Exception e) {}

        return Constantes.EMPTY;
    }

    /**
     *
     * @param socket
     * @return
     * @throws IOException
     */
    private String read(final HttpURLConnection socket) throws IOException {
        InputStream is = null;
        String inputLine;

        try
        {
            is = socket.getInputStream();

            final BufferedReader in     = new BufferedReader(new InputStreamReader(is));
            final StringBuilder  result = new StringBuilder();

            while ((inputLine = in.readLine()) != null) result.append(inputLine);

            return result.toString();
        }
        finally
        {
            if (is != null) is.close();
        }
    }

    /**
     * Constructor
     * @param x
     */
    HttpClient(final String x)
    {
        endpoint = String.valueOf(x).trim();

        if (Constantes.EMPTY.equals(endpoint) == true)
        {
            log.warn("CassoaConnector: endpoint nulo!!");
            throw new IllegalArgumentException("Endpoint nulo");
        }
    }

    /**
     * Conexion HTTP GET.
     * @param url
     * @param headers
     * @return
     */
    public String get(final java.util.Map<String, String> headers) throws IOException {
        HttpsURLConnection socket = null;

        try
        {
            //1.- Se abre conexion
            socket = (HttpsURLConnection) new java.net.URL(endpoint).openConnection();

            //2.- Se establecen las cabeceras
            if (headers != null)
            {
                for (Map.Entry<String, String> entry: headers.entrySet())
                {
                    socket.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }

            //3.- Obtenemos la respuesta
            if (socket.getResponseCode() != 200)
            {
                log.warn("CassoaConnector: " + this.getError(socket));
                throw new IOException(this.getError(socket));
            }

            String response = read(socket);

            //4.- Fin
            return response;
        }
        catch (IOException e) {throw e;}
        finally
        {
            if (socket != null) socket.disconnect();
        }
    }
}
