package com.sonereker.linkconverter.page;

import com.sonereker.linkconverter.page.type.PageType;

import java.util.Set;

/**
 * PageService is responsible for registering page types and applying filters on them.
 */
public interface PageService {
    /**
     * Saves given pageType to <code>PAGE_TYPES</code> set. This happens during app init process.
     *
     * @param pageType {@link com.sonereker.linkconverter.page.type.PageType} to register
     */
    void registerPage(PageType pageType);

    /**
     * For testing; returns registered PAGE_TYPES
     *
     * @return unmodifiable copy of PAGE_TYPES
     */
    Set<PageType> getPageTypes();

    /**
     * For testing; clears PAGE_TYPES
     */
    void unRegisterAllPageTypes();

    /**
     * Finds {@link com.sonereker.linkconverter.page.type.PageType} having the type provided in deepLink's <code>Page</code> param.
     *
     * @param deepLink deepLink string.
     * @return {@link com.sonereker.linkconverter.page.type.PageType}
     */
    PageType findPageTypeByDeepLink(String deepLink);

    /**
     * Finds page type with matching url pattern for the given url
     *
     * @param url to find PageType of
     * @return PageType
     */
    PageType findPageTypeByUrl(String url);
}
