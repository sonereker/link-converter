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
class HomePageTest {

    private static final String STORE_HOSTNAME = "http://www.dummy-store.com";
    private HomePage homePage;

    private final PageService pageService = new PageServiceImpl();

    @BeforeEach
    void setUp() {
        homePage = new HomePage(pageService);
        homePage.setAppUrl(STORE_HOSTNAME);
    }

    @Test
    void whenRegister_thenRegisterToPageService() {
        homePage.register();
        assertTrue(pageService.getPageTypes().contains(homePage));
        pageService.unRegisterAllPageTypes();
    }

    @ParameterizedTest
    @MethodSource("provideUrlDeepLinkMappings")
    void givenUrl_whenDenerateDeepLinkFromUrl_thenReturnDeepLink(String url, String deepLink) {
        String generatedDeepLink = homePage.generateDeepLinkFromUrl(url);
        assertThat(generatedDeepLink, is(deepLink));
    }

    @ParameterizedTest
    @MethodSource("provideDeepLinkUrlMappings")
    void givenUrl_whenDenerateUrlFromDeepLink_thenReturnUrl(String deepLink, String url) {
        String generatedUrl = homePage.generateUrlFromDeepLink(deepLink);
        assertThat(generatedUrl, is(url));
    }

    private static Stream<Arguments> provideUrlDeepLinkMappings() {
        return Stream.of(
                Arguments.of("https://www.store.com/account/favorites", "st://?Page=Home"),
                Arguments.of("https://www.store.com/account/#/orders", "st://?Page=Home"),
                Arguments.of("https://www.store.com/", "st://?Page=Home")
        );
    }

    private static Stream<Arguments> provideDeepLinkUrlMappings() {
        return Stream.of(
                Arguments.of("st://?Page=Home", STORE_HOSTNAME),
                Arguments.of("st://?Page=Favorites", STORE_HOSTNAME),
                Arguments.of("st://?Page=Orders", STORE_HOSTNAME)
        );
    }
}
