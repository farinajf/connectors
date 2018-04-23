/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wso2.carbon.esb.connector.oauth;

import java.io.IOException;

/**
 *
 * @author fran
 */
public interface OAuth2Service {
    String getUSerProfile(final String endpoint, final String accessToken) throws IOException;
    boolean hasRole      (final String response, final String allowedRole);
}
