package com.jivesoftware.os.upena.deployable.server;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * Created by jonathan.colt on 11/14/16.
 */
public class UpenaFormeAuthenticationFilter extends FormAuthenticationFilter {
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, false);
    }
}
