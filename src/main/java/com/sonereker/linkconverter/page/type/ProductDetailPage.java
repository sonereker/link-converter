package com.sonereker.linkconverter.page.type;

import com.sonereker.linkconverter.page.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * ProductDetailPage
 */
@Component
public class ProductDetailPage implements PageType {
    private static final Predicate<String> URL_PATTERN_PREDICATE =
            Pattern.compile("^http(s)://[a-z-:0-9.]*/[a-z]{2,255}/[a-z-]*-p-[0-9]{5,10}\\??([a-zA-Z0-9=&]*)$").asPredicate();

    private static final String PRODUCT_INFO_SEGMENT_SPLITTER = "-p-";
    private static final String PAGE_TYPE = "Product";
    private static final String PARAM_CONTENT_ID = "ContentId";
    private static final String PARAM_DISCOUNT_ID = "DiscountId";
    private static final String PARAM_SELLER_ID = "SellerId";
    private static final String PARAM_STORE_ID = "storeId";
    private static final String PARAM_URL_SELLER_ID = "sellerId";
    private static final String PATH_SEGMENT_BRAND = "brand";
    private static final String PRODUCT_IDENTIFIER_PREFIX = "product-p-";

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
    public ProductDetailPage(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public String generateDeepLinkFromUrl(String url) {
        var uriComponents = UriComponentsBuilder.fromUriString(url).build();
        var productInfoSegment = uriComponents.getPathSegments().get(1);
        var productValues = productInfoSegment.split(PRODUCT_INFO_SEGMENT_SPLITTER);
        var queryParams = uriComponents.getQueryParams();

        return UriComponentsBuilder.newInstance()
                .scheme(DEEP_LINK_SCHEME)
                .host("")
                .queryParam(PARAM_PAGE, PAGE_TYPE)
                .queryParam(PARAM_CONTENT_ID, productValues[1])
                .queryParamIfPresent(PARAM_DISCOUNT_ID, Optional.ofNullable(queryParams.getFirst(PARAM_STORE_ID)))
                .queryParamIfPresent(PARAM_SELLER_ID, Optional.ofNullable(queryParams.getFirst(PARAM_URL_SELLER_ID)))
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
                .pathSegment(PATH_SEGMENT_BRAND)
                .path(PRODUCT_IDENTIFIER_PREFIX + queryParams.getFirst(PARAM_CONTENT_ID))
                .queryParamIfPresent(PARAM_STORE_ID, Optional.ofNullable(queryParams.getFirst(PARAM_DISCOUNT_ID)))
                .queryParamIfPresent(PARAM_URL_SELLER_ID, Optional.ofNullable(queryParams.getFirst(PARAM_SELLER_ID)))
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
