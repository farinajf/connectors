/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wso2.carbon.esb.connector.oauth;

import java.io.IOException;
import org.wso2.carbon.esb.connector.oauth.exceptions.HTTPClientException;

/**
 *
 * @author fran
 */
public interface OAuth2Service {
    String getProfile(final String endpoint, final String accessToken) throws HTTPClientException, IOException;
    boolean hasRole  (final String response, final String allowedRole);
}
