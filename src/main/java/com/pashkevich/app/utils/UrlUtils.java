package com.pashkevich.app.utils;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

import static com.pashkevich.app.constants.Constants.Common.EMPTY;

/**
 * Created by Vlad on 25.03.17.
 */
public class UrlUtils {

    public static String getAppUrl(WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
        String url = EMPTY;
        url += httpServletRequest.getScheme();
        url += "://";
        url += httpServletRequest.getServerName();
        url += ":";
        url += httpServletRequest.getServerPort();
        if (!httpServletRequest.getContextPath().isEmpty()) {
            url += "/";
            url += httpServletRequest.getContextPath();
        }
        return url;
    }

    public static String getPageUrl(WebRequest request, String username, Long blogId) {
        return getAppUrl(request) + "/" + username + "/" + blogId + "/home";
    }

}
