package org.wso2.carbon.esb.connector.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OAuth2ProviderImpl implements OAuth2Provider {
	private static final Pattern CN_PATTERN = Pattern.compile("^CN=([^,]+)");
	
	private final Log log = LogFactory.getLog(this.getClass());
	private final HttpClient httpClient;
    private final String     endpoint;
    
    private Collection<String> rolesFromLdapCn(Collection<String> scopes) {
        final Collection<String> roles = new ArrayList<String>();
        for (String scope : scopes) {
            final Matcher matcher = CN_PATTERN.matcher(scope);
            if( matcher.find() ) {
                final String cnValue = matcher.group(1);
                roles.add( cnValue.trim().toLowerCase() );
            }
        }
        
        return roles;
    }

    /**
     * 
     * @param httpClient
     * @param endpoint https://cassoa.dev.xunta.local/oidc/profile
     */
    public OAuth2ProviderImpl(final HttpClient httpClient, final String endpoint) {
    	this.httpClient = httpClient;
    	this.endpoint   = endpoint;
    }
    
    /**
     * 
     */
	@Override
	public Profile getUSerProfile(String accessToken) throws JsonParseException, IOException {
		log.info("OAuth2ProviderImpl.getUSerProfile(" + accessToken + ")");

		final java.util.Map<String,String> headers = new LinkedHashMap<String,String>();

		headers.put("Authorization", accessToken);

		final String response = httpClient.get(endpoint, headers);
		Json json = new Json(response);

		final String                 subject   = json.getString("sub");
		final java.util.List<String> ldapRoles = json.getArray("roles");
		final Collection<String>     roles     = rolesFromLdapCn(ldapRoles);

		return new Profile(subject, roles);
	}
}
