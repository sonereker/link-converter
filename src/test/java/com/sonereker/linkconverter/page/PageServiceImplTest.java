package com.sonereker.linkconverter.page;

import com.sonereker.linkconverter.page.type.HomePage;
import com.sonereker.linkconverter.page.type.PageType;
import com.sonereker.linkconverter.page.type.ProductDetailPage;
import com.sonereker.linkconverter.page.type.SearchPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.sonereker.linkconverter.page.PageServiceImpl.MULTIPLE_DEFAULT_PAGES_ERR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageServiceImplTest {

    @Mock
    private PageType dummyDefaultPageType;

    private PageService pageService;

    @BeforeEach
    void initService() {
        pageService = new PageServiceImpl();
    }

    @AfterEach
    void cleanUp() {
        pageService.unRegisterAllPageTypes();
    }

    @Test
    void givenProductDetailPage_whenRegister_thenRegisterPageType() {
        var productDetailPage = new ProductDetailPage(pageService);
        pageService.registerPage(productDetailPage);

        var pageTypeRegistered = pageService.getPageTypes().contains(productDetailPage);
        assertTrue(pageTypeRegistered);
    }

    @Test
    void givenMultipleDefaultPages_whenRegister_thenThrowException() {
        registerAllPages();

        when(dummyDefaultPageType.isDefault()).thenReturn(true);
        var exception = assertThrows(IllegalArgumentException.class,
                () -> pageService.registerPage(dummyDefaultPageType));

        assertThat(exception.getMessage(), is(MULTIPLE_DEFAULT_PAGES_ERR));
    }

    @ParameterizedTest
    @MethodSource("provideDeepLinkPageMappings")
    void givenDeepLink_whenFindPageTypeByDeepLink_thenReturnMatchingPage(String deepLink, Class<PageType> pageTypeClassName) {
        registerAllPages();

        var pageType = pageService.findPageTypeByDeepLink(deepLink);
        assertEquals(pageTypeClassName, pageType.getClass());
    }

    @ParameterizedTest
    @MethodSource("provideUrlPageMappings")
    void givenUrl_whenFindPageTypeByUrl_thenReturnMatchingPage(String url, Class<PageType> pageTypeClassName) {
        registerAllPages();

        var pageType = pageService.findPageTypeByUrl(url);
        assertEquals(pageTypeClassName, pageType.getClass());
    }

    private void registerAllPages() {
        if (pageService.getPageTypes().isEmpty()) {
            var productDetailPage = new ProductDetailPage(pageService);
            var searchPage = new SearchPage(pageService);
            var homePage = new HomePage(pageService);
            pageService.registerPage(homePage);
            pageService.registerPage(productDetailPage);
            pageService.registerPage(searchPage);
        }
    }

    private static Stream<Arguments> provideDeepLinkPageMappings() {
        return Stream.of(
                Arguments.of("st://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064", ProductDetailPage.class),
                Arguments.of("st://?Page=Product&ContentId=1925865", ProductDetailPage.class),
                Arguments.of("st://?Page=Product&ContentId=1925865&CampaignId=439892", ProductDetailPage.class),
                Arguments.of("st://?Page=Product&ContentId=1925865&MerchantId=105064", ProductDetailPage.class),
                Arguments.of("st://?Page=Search&Query=elbise", SearchPage.class),
                Arguments.of("st://?Page=Search&Query=%C3%BCt%C3%BC", SearchPage.class),
                Arguments.of("st://?Page=Favorites", HomePage.class),
                Arguments.of("st://?Page=Orders", HomePage.class)
        );
    }

    private static Stream<Arguments> provideUrlPageMappings() {
        return Stream.of(
                Arguments.of("https://www.store.com/casio/saat-p-1925865?boutiqueId=439892&merchantId=105064", ProductDetailPage.class),
                Arguments.of("https://www.store.com/casio/erkek-kol-saati-p-1925865", ProductDetailPage.class),
                Arguments.of("https://www.store.com/casio/erkek-kol-saati-p-1925865?boutiqueId=439892", ProductDetailPage.class),
                Arguments.of("https://www.store.com/casio/erkek-kol-saati-p-1925865?merchantId=105064", ProductDetailPage.class),
                Arguments.of("https://www.store.com/all-products?q=elbise", SearchPage.class),
                Arguments.of("https://www.store.com/all-products?q=%C3%BCt%C3%BC", SearchPage.class),
                Arguments.of("https://www.store.com/account/favorites", HomePage.class),
                Arguments.of("https://www.store.com/account/#/orders", HomePage.class),
                Arguments.of("https://www.store.com/", HomePage.class)
        );
    }

}
