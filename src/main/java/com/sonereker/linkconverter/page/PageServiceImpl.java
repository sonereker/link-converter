package com.sonereker.linkconverter.page;

import com.sonereker.linkconverter.exception.MissingDefaultPageException;
import com.sonereker.linkconverter.exception.UrlPatternNotFoundException;
import com.sonereker.linkconverter.page.type.PageType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.Set;

import static com.sonereker.linkconverter.page.type.PageType.PARAM_PAGE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class PageServiceImpl implements PageService {
    private static final Set<PageType> PAGE_TYPES = new HashSet<>();

    @Override
    public void registerPage(PageType pageType) {
        var defaultPageExists = PAGE_TYPES.stream()
                .anyMatch(PageType::isDefault);

        if (pageType.isDefault() && defaultPageExists) {
            throw new IllegalArgumentException("There are multiple default page types.");
        }

        PAGE_TYPES.add(pageType);
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
                .orElseThrow(UrlPatternNotFoundException::new);
    }

    private PageType getDefaultPageType() {
        return PAGE_TYPES.stream()
                .filter(PageType::isDefault)
                .findFirst()
                .orElseThrow(MissingDefaultPageException::new);
    }
}
