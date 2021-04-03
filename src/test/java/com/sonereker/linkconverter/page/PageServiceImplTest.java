package com.sonereker.linkconverter.page;

import com.sonereker.linkconverter.page.type.PageType;
import com.sonereker.linkconverter.page.type.ProductDetailPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.sonereker.linkconverter.page.PageServiceImpl.MULTIPLE_DEFAULT_PAGES_ERR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageServiceImplTest {

    @Mock
    private PageType dummyDefaultPageType1;

    @Mock
    private PageType dummyDefaultPageType2;

    private PageService pageService;

    @BeforeEach
    void initService() {
        pageService = new PageServiceImpl();
    }

    @Test
    void givenProductDetailPage_whenRegister_thenRegisterPageType() {
        var productDetailPage = new ProductDetailPage(pageService);
        pageService.registerPage(productDetailPage);

        var pageTypeRegistered = PageServiceImpl.PAGE_TYPES.contains(productDetailPage);
        assertTrue(pageTypeRegistered);
    }

    @Test
    void givenMultipleDefaultPages_whenRegister_thenThrowException() {
        when(dummyDefaultPageType1.isDefault()).thenReturn(true);
        when(dummyDefaultPageType2.isDefault()).thenReturn(true);

        pageService.registerPage(dummyDefaultPageType1);
        var exception = assertThrows(IllegalArgumentException.class,
                () -> pageService.registerPage(dummyDefaultPageType2));

        assertEquals(MULTIPLE_DEFAULT_PAGES_ERR, exception.getMessage());
    }
}
