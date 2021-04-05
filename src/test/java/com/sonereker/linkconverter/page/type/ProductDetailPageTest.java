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
class ProductDetailPageTest {

    private static final String STORE_HOSTNAME = "www.dummy-store.com";
    private static final String STORE_SCHEME = "http";
    private static final String STORE_URL = "%s://%s".formatted(STORE_SCHEME, STORE_HOSTNAME);
    private ProductDetailPage productDetailPage;

    private final PageService pageService = new PageServiceImpl();

    @BeforeEach
    void setUp() {
        productDetailPage = new ProductDetailPage(pageService);
        productDetailPage.setAppHost(STORE_HOSTNAME);
        productDetailPage.setAppScheme(STORE_SCHEME);
    }

    @Test
    void whenRegister_thenRegisterToPageService() {
        productDetailPage.register();
        assertTrue(pageService.getPageTypes().contains(productDetailPage));
        pageService.unRegisterAllPageTypes();
    }

    @ParameterizedTest
    @MethodSource("provideUrlDeepLinkMappings")
    void givenUrl_whenDenerateDeepLinkFromUrl_thenReturnDeepLink(String url, String deepLink) {
        String generatedDeepLink = productDetailPage.generateDeepLinkFromUrl(url);
        assertThat(generatedDeepLink, is(deepLink));
    }

    @ParameterizedTest
    @MethodSource("provideDeepLinkUrlMappings")
    void givenUrl_whenDenerateUrlFromDeepLink_thenReturnUrl(String deepLink, String url) {
        String generatedUrl = productDetailPage.generateUrlFromDeepLink(deepLink);
        assertThat(generatedUrl, is(url));
    }

    private static Stream<Arguments> provideUrlDeepLinkMappings() {
        return Stream.of(
                Arguments.of(STORE_URL + "/tissot/watch-p-12345?storeId=10001&sellerId=20001", "st://?Page=Product&ContentId=12345&DiscountId=10001&SellerId=20001"),
                Arguments.of(STORE_URL + "/tissot/mens-watches-p-12345", "st://?Page=Product&ContentId=12345"),
                Arguments.of(STORE_URL + "/tissot/mens-watches-p-12345?storeId=10001", "st://?Page=Product&ContentId=12345&DiscountId=10001"),
                Arguments.of(STORE_URL + "/tissot/mens-watches-p-12345?sellerId=20001", "st://?Page=Product&ContentId=12345&SellerId=20001")
        );
    }

    private static Stream<Arguments> provideDeepLinkUrlMappings() {
        return Stream.of(
                Arguments.of("st://?Page=Product&ContentId=12345&DiscountId=10001&SellerId=20001", STORE_URL + "/brand/product-p-12345?storeId=10001&sellerId=20001"),
                Arguments.of("st://?Page=Product&ContentId=12345", STORE_URL + "/brand/product-p-12345"),
                Arguments.of("st://?Page=Product&ContentId=12345&DiscountId=10001", STORE_URL + "/brand/product-p-12345?storeId=10001"),
                Arguments.of("st://?Page=Product&ContentId=12345&SellerId=20001", STORE_URL + "/brand/product-p-12345?sellerId=20001")
        );
    }
}
