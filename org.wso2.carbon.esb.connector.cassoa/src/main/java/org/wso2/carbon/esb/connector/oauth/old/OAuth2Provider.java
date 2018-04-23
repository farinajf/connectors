package org.wso2.carbon.esb.connector.oauth;

import java.io.IOException;

public interface OAuth2Provider {
	Profile getUSerProfile(final String accessToken) throws JsonParseException, IOException;
}
