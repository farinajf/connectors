package org.wso2.carbon.esb.connector.oauth;

import java.util.Collection;

public class Profile {
    private final String subject;
    private final Collection<String> roles;

    /**
     *
     * @param subject
     * @param roles
     */
    public Profile(final String subject, Collection<String> roles) {
        this.subject = subject;
        this.roles   = roles;
    }

    public String             getSubject() {return this.subject;}
    public Collection<String> getRoles()   {return this.roles;}

    /**
     *
     * @param allowedRole
     * @return
     */
    public boolean hasRole(final String allowedRole) {
        if ((allowedRole == null) || (allowedRole.trim().isEmpty() == true)) return false;

        for (String x : roles) {
            if (x.contains(allowedRole) == true) return true;
        }

        return false;
    }

    /**
     *
     */
    public String toString() {
        return "Profile {subject:" + this.subject + ", roles:" + this.roles + "}";
    }
}
