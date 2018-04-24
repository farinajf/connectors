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
import org.wso2.carbon.esb.connector.oauth.exceptions.HTTPClientException;
import org.wso2.carbon.esb.connector.oauth.exceptions.JsonParseException;

/**
 *
 * @author fran
 */
public class OAuth2ServiceImpl implements OAuth2Service {
    private static final Pattern CN_PATTERN = Pattern.compile("^CN=([^,]+)");
    private static final String  SUB        = "sub";
    private static final String  ROLES      = "roles";

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
    public String getProfile(final String endpoint, String accessToken) throws HTTPClientException, IOException {
        final java.util.Map<String,String> headers = new LinkedHashMap<String,String>();

        log.info("OAuth2ServiceImpl.getProfile(" + accessToken + ")");

        //1.- Se construye la cabecera
        headers.put(Constantes.TP_AUTHORIZATION, accessToken);

        //2.- Cliente HTTP
        final HttpClient client = new HttpClient(endpoint);

        //3.- Se valida el token
        return client.get(headers);
    }

    @Override
    public boolean hasRole(String response, String allowedRole) throws JsonParseException{
        log.info("OAuth2ServiceImpl.hasRole(" + allowedRole + "," + response + ")");

        //0.- Control de errores
        if (Constantes.isEmpty(response)    == true) return false;
        if (Constantes.isEmpty(allowedRole) == true) return false;

        //1.- Parseo JSON
        final Json                   json      = new Json(response);
        final String                 subject   = json.getString(SUB);
        final java.util.List<String> ldapRoles = json.getArray (ROLES);
	final Collection<String>     roles     = rolesFromLdapCn(ldapRoles);

        final Profile profile = new Profile(subject, roles);

        log.info("OAuth2ServiceImpl.profile: [" + profile.toString() + "]");

        return profile.hasRole(allowedRole);
    }
}
