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
import org.wso2.carbon.esb.connector.oauth.exceptions.HTTPClientException;

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
        endpoint = Constantes.trimToEmpty(x);

        if (Constantes.EMPTY.equals(endpoint) == true)
        {
            log.error("CassoaConnector: endpoint nulo!!");
            throw new IllegalArgumentException("CassoaConnector: endpoint nulo!!");
        }
    }

    /**
     * Conexion HTTP GET.
     * @param url
     * @param headers
     * @return
     * @throws HTTPClientException
     * @throws IOException
     */
    public String get(final java.util.Map<String, String> headers) throws HTTPClientException, IOException {
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
                throw new HTTPClientException(socket.getResponseCode(), socket.getResponseMessage());
            }

            //4.- Fin
            return this.read(socket);
        }
        finally
        {
            if (socket != null) socket.disconnect();
        }
    }
}
