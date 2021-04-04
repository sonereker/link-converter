package com.sonereker.linkconverter.page;

import com.sonereker.linkconverter.exception.MissingDefaultPageException;
import com.sonereker.linkconverter.page.type.PageType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.Set;

import static com.sonereker.linkconverter.page.type.PageType.PARAM_PAGE;
import static java.util.Collections.unmodifiableSet;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PageServiceImpl implements PageService {
    private static final Set<PageType> PAGE_TYPES = new HashSet<>();
    static final String MULTIPLE_DEFAULT_PAGES_ERR = "There are multiple default page types.";

    @Override
    public void registerPage(PageType pageType) {
        var defaultPageExists = PAGE_TYPES.stream()
                .anyMatch(PageType::isDefault);

        if (pageType.isDefault() && defaultPageExists) {
            throw new IllegalArgumentException(MULTIPLE_DEFAULT_PAGES_ERR);
        }

        PAGE_TYPES.add(pageType);
    }

    @Override
    public void unRegisterAllPageTypes() {
        PAGE_TYPES.clear();
    }

    @Override
    public Set<PageType> getPageTypes() {
        return unmodifiableSet(PAGE_TYPES);
    }

    @Override
    public PageType findPageTypeByDeepLink(String deepLink) {
        var uri = UriComponentsBuilder.fromUriString(deepLink).build();
        var queryParams = uri.getQueryParams();
        var page = queryParams.getFirst(PARAM_PAGE);

        return PAGE_TYPES.stream()
                .filter(p -> p.getPageType().equals(page))
                .findFirst()
                .orElse(getDefaultPageType());
    }

    @Override
    public PageType findPageTypeByUrl(String url) {
        return PAGE_TYPES.stream()
                .filter(p -> p.getUrlPatternPredicate().test(url))
                .findFirst()
                .orElse(getDefaultPageType());
    }

    private PageType getDefaultPageType() {
        return PAGE_TYPES.stream()
                .filter(PageType::isDefault)
                .findFirst()
                .orElseThrow(MissingDefaultPageException::new);
    }
}
