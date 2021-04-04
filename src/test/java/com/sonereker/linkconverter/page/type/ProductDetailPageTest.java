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
                Arguments.of(STORE_URL + "/casio/saat-p-1925865?boutiqueId=439892&merchantId=105064", "st://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064"),
                Arguments.of(STORE_URL + "/casio/erkek-kol-saati-p-1925865", "st://?Page=Product&ContentId=1925865"),
                Arguments.of(STORE_URL + "/casio/erkek-kol-saati-p-1925865?boutiqueId=439892", "st://?Page=Product&ContentId=1925865&CampaignId=439892"),
                Arguments.of(STORE_URL + "/casio/erkek-kol-saati-p-1925865?merchantId=105064", "st://?Page=Product&ContentId=1925865&MerchantId=105064")
        );
    }

    private static Stream<Arguments> provideDeepLinkUrlMappings() {
        return Stream.of(
                Arguments.of("st://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064", STORE_URL + "/brand/name-p-1925865?boutiqueId=439892&merchantId=105064"),
                Arguments.of("st://?Page=Product&ContentId=1925865", STORE_URL + "/brand/name-p-1925865"),
                Arguments.of("st://?Page=Product&ContentId=1925865&CampaignId=439892", STORE_URL + "/brand/name-p-1925865?boutiqueId=439892"),
                Arguments.of("st://?Page=Product&ContentId=1925865&MerchantId=105064", STORE_URL + "/brand/name-p-1925865?merchantId=105064")
        );
    }
}
