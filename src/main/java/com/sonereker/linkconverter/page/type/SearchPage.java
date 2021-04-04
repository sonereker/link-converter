package com.sonereker.linkconverter.page.type;

import com.sonereker.linkconverter.page.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class SearchPage implements PageType {
    private static final Predicate<String> URL_PATTERN_PREDICATE =
            Pattern.compile("^https://[a-z-:0-9.]*/all-products\\?q=[a-zA-Z0-9%]*$").asPredicate();

    private static final String PAGE_TYPE = "Search";
    private static final String PARAM_QUERY = "Query";
    private static final String QUERY_KEY = "q";
    private static final String ALL_PRODUCTS_PATH = "all-products";

    private final PageService pageService;

    private String appScheme;
    private String appHost;

    @Value("${app.scheme}")
    public void setAppScheme(String appScheme) {
        this.appScheme = appScheme;
    }

    @Value("${app.host}")
    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    @Autowired
    public SearchPage(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public String generateDeepLinkFromUrl(String url) {
        var uriComponents = UriComponentsBuilder.fromUriString(url).build();
        var queryParams = uriComponents.getQueryParams();

        return UriComponentsBuilder.newInstance()
                .scheme(DEEP_LINK_SCHEME)
                .host("")
                .queryParam(PARAM_PAGE, PAGE_TYPE)
                .queryParam(PARAM_QUERY, queryParams.getFirst(QUERY_KEY))
                .build()
                .toUriString();
    }

    @Override
    public String generateUrlFromDeepLink(String deepLink) {
        var uri = UriComponentsBuilder.fromUriString(deepLink).build();
        var queryParams = uri.getQueryParams();

        return UriComponentsBuilder.newInstance()
                .scheme(appScheme)
                .host(appHost)
                .path(ALL_PRODUCTS_PATH)
                .queryParam(QUERY_KEY, queryParams.getFirst(PARAM_QUERY))
                .build()
                .toUriString();
    }

    @Override
    public Predicate<String> getUrlPatternPredicate() {
        return URL_PATTERN_PREDICATE;
    }

    @Override
    public String getPageType() {
        return PAGE_TYPE;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @PostConstruct
    @Override
    public void register() {
        pageService.registerPage(this);
    }
}
