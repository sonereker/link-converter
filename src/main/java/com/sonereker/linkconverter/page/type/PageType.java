package com.sonereker.linkconverter.page.type;

import java.net.MalformedURLException;
import java.util.function.Predicate;

/**
 * Interface for PageTypes
 */
public interface PageType {
    String DEEP_LINK_SCHEME = "st";
    String PARAM_PAGE = "Page";

    /**
     * Generates deepLink from given url
     *
     * @param url A valid url string to convert to deepLink
     * @return deepLink
     */
    String generateDeepLinkFromUrl(String url) throws MalformedURLException;

    /**
     * Generates url from given deepLink
     *
     * @param deepLink A valid deepLink string to convert to url
     * @return url
     */
    String generateUrlFromDeepLink(String deepLink);

    /**
     * Url pattern predicate which is responsible for finding if given url belongs to current PageType
     *
     * @return <code>URL_PATTERN_PREDICATE</code>
     */
    Predicate<String> getUrlPatternPredicate();

    /**
     * PageType of current PageType object which is actually defined in DeepLink's Page param
     *
     * @return PageType of current PageType
     */
    String getPageType();

    /**
     * Default PageType is responsible for all operations which are not handled by other PageTypes
     * There should be only one default PageType otherwise a runtime exception will be thrown on app init process
     *
     * @return <code>true</code> or <code>false</code>
     */
    boolean isDefault();

    /**
     * Registers PageType to PageService
     */
    void register();
}
