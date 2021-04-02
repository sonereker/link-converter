package com.sonereker.linkconverter.page.type;

import com.sonereker.linkconverter.page.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * HomePage a.k.a. DefaultPage
 */
@Slf4j
@Order
@Component
public class HomePage implements PageType {
    private static final Predicate<String> URL_PATTERN_PREDICATE =
            Pattern.compile("^https://www.store.com/.*$").asPredicate();

    public static final String PAGE_TYPE = "Home";

    private final PageService pageService;

    @Value("${app.scheme}://${app.host}")
    private String appUrl;

    @Autowired
    public HomePage(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public String generateDeepLinkFromUrl(String url) throws MalformedURLException {
        return UriComponentsBuilder.newInstance()
                .scheme(DEEP_LINK_SCHEME)
                .host("")
                .queryParam(PARAM_PAGE, PAGE_TYPE)
                .build()
                .toUriString();
    }

    @Override
    public String generateUrlFromDeepLink(String deepLink) {
        return appUrl;
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
        return true;
    }

    @PostConstruct
    @Override
    public void register() {
        pageService.registerPage(this);
    }
}
