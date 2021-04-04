package com.sonereker.linkconverter.page.type;

import com.sonereker.linkconverter.page.PageService;
import com.sonereker.linkconverter.page.PageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SearchPageTest {
    private static final String STORE_HOSTNAME = "www.dummy-store.com";
    private static final String STORE_SCHEME = "http";
    private static final String STORE_URL = "%s://%s".formatted(STORE_SCHEME, STORE_HOSTNAME);
    private SearchPage searchPage;

    private final PageService pageService = new PageServiceImpl();

    @BeforeEach
    void setUp() {
        searchPage = new SearchPage(pageService);
        searchPage.setAppHost(STORE_HOSTNAME);
        searchPage.setAppScheme(STORE_SCHEME);
    }

    @Test
    void whenRegister_thenRegisterToPageService() {
        searchPage.register();
        assertTrue(pageService.getPageTypes().contains(searchPage));
        pageService.unRegisterAllPageTypes();
    }

    @ParameterizedTest
    @MethodSource("provideUrlDeepLinkMappings")
    void givenUrl_whenDenerateDeepLinkFromUrl_thenReturnDeepLink(String url, String deepLink) {
        String generatedDeepLink = searchPage.generateDeepLinkFromUrl(url);
        assertThat(generatedDeepLink, is(deepLink));
    }

    @ParameterizedTest
    @MethodSource("provideDeepLinkUrlMappings")
    void givenUrl_whenDenerateUrlFromDeepLink_thenReturnUrl(String deepLink, String url) {
        String generatedUrl = searchPage.generateUrlFromDeepLink(deepLink);
        assertThat(generatedUrl, is(url));
    }

    private static Stream<Arguments> provideUrlDeepLinkMappings() {
        return Stream.of(
                Arguments.of(STORE_URL + "/all-products?q=elbise", "st://?Page=Search&Query=elbise"),
                Arguments.of(STORE_URL + "/all-products?q=%C3%BCt%C3%BC", "st://?Page=Search&Query=%C3%BCt%C3%BC")
        );
    }

    private static Stream<Arguments> provideDeepLinkUrlMappings() {
        return Stream.of(
                Arguments.of("st://?Page=Search&Query=elbise", STORE_URL + "/all-products?q=elbise"),
                Arguments.of("st://?Page=Search&Query=%C3%BCt%C3%BC", STORE_URL + "/all-products?q=%C3%BCt%C3%BC")
        );
    }
}
