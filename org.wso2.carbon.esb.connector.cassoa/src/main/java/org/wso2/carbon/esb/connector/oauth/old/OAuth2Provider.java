package org.wso2.carbon.esb.connector.oauth.old;

import java.io.IOException;
import org.wso2.carbon.esb.connector.oauth.JsonParseException;

public interface OAuth2Provider {
	Object getUSerProfile(final String accessToken) throws JsonParseException, IOException;
}
