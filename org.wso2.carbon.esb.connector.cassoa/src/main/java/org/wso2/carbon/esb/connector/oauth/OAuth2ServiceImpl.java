/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wso2.carbon.esb.connector.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fran
 */
public class OAuth2ServiceImpl implements OAuth2Service {
    private static final Pattern CN_PATTERN = Pattern.compile("^CN=([^,]+)");

    private final Log log = LogFactory.getLog(this.getClass());

    /**
     *
     * @param scopes
     * @return
     */
    private Collection<String> rolesFromLdapCn(Collection<String> scopes) {
        final Collection<String> roles = new ArrayList<String>();

        for (String scope : scopes)
        {
            final Matcher matcher = CN_PATTERN.matcher(scope);
            if (matcher.find())
            {
                final String cnValue = matcher.group(1);
                roles.add(cnValue.trim().toLowerCase());
            }
        }

        return roles;
    }

    @Override
    public String getUSerProfile(final String endpoint, String accessToken) throws IOException {
        final java.util.Map<String,String> headers = new LinkedHashMap<String,String>();

        log.info("OAuth2ProviderImpl.getUSerProfile(" + accessToken + ")");

        //1.- Se construye la cabecera
        headers.put(Constantes.TP_AUTHORIZATION, accessToken);

        //2.- Cliente HTTP
        final HttpClient client = new HttpClient(endpoint);

        //3.- Se valida el token
        return client.get(headers);
    }

    @Override
    public boolean hasRole(String response, String allowedRole) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
