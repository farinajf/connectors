package org.wso2.carbon.esb.connector.oauth.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

class HttpClient {
	
	private void setHeaders(java.util.Map<String, String> headers, HttpsURLConnection con) {
        if (headers != null) {
            for (java.util.Map.Entry<String, String> entry : headers.entrySet()) {
                con.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }
	
	private String read(InputStream input) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        return response.toString();
    }

    private void assertResponseStatusOk(int responseCode) throws IOException {
        if (responseCode != 200) {
            throw new IOException("Código de estado no esperado: " + responseCode);
        }
    }
	
	public String get(String url, java.util.Map<String, String> headers) throws IOException {
        HttpsURLConnection con = null;
        try {
            java.net.URL obj = new java.net.URL(url);
            con = (HttpsURLConnection) obj.openConnection();
            setHeaders(headers, con);

            int responseCode = con.getResponseCode();

            assertResponseStatusOk(responseCode);

            String response = read(con.getInputStream());
            con.getInputStream().close();

            return response;
        } catch (IOException e) {
            throw e;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
