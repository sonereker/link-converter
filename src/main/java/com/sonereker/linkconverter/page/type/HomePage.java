package com.sonereker.linkconverter.page.type;

import com.sonereker.linkconverter.page.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.function.Predicate;

/**
 * HomePage a.k.a. DefaultPage
 */
@Component
public class HomePage implements PageType {
    public static final String PAGE_TYPE = "Home";

    private final PageService pageService;

    private String appUrl;

    @Value("${app.scheme}://${app.host}")
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @Autowired
    public HomePage(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public String generateDeepLinkFromUrl(String url) {
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
        return s -> false; // URL matching is not wanted for default PageType
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
