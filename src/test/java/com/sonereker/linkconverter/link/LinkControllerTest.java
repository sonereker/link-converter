package com.sonereker.linkconverter.link;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonereker.linkconverter.page.PageServiceImpl;
import com.sonereker.linkconverter.page.type.ProductDetailPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(LinkController.class)
class LinkControllerTest {

    private static final String DUMMY_URL = "https://dummy-url.com";
    private static final String DUMMY_DEEP_LINK = "dl://?Page=Dummy";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PageServiceImpl pageServiceImpl;

    @MockBean
    private ProductDetailPage productDetailPage;

    @MockBean
    private LinkConversionRepository linkConversionRepository;

    @Test
    void givenUrl_whenConvertToDeepLink_thenReturnDeepLink() throws Exception {
        given(pageServiceImpl.findPageTypeByUrl(DUMMY_URL)).willReturn(productDetailPage);
        given(productDetailPage.generateDeepLinkFromUrl(DUMMY_URL)).willReturn(DUMMY_DEEP_LINK);
        doReturn(null).when(linkConversionRepository).save(any());

        var linkDto = new LinkDto(DUMMY_URL, DUMMY_DEEP_LINK);

        mvc.perform(MockMvcRequestBuilders.post("/links/convert-to-deeplink")
                .content(objectMapper.writeValueAsBytes(linkDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("url", is(equalTo(DUMMY_URL))))
                .andExpect(MockMvcResultMatchers.jsonPath("deepLink", is(equalTo(DUMMY_DEEP_LINK))));
    }

    @Test
    void givenDeepLink_whenConvertToUrl_thenReturnUrl() throws Exception {
        given(pageServiceImpl.findPageTypeByDeepLink(DUMMY_DEEP_LINK)).willReturn(productDetailPage);
        given(productDetailPage.generateUrlFromDeepLink(DUMMY_DEEP_LINK)).willReturn(DUMMY_URL);
        doReturn(null).when(linkConversionRepository).save(any());

        var linkDto = new LinkDto(DUMMY_URL, DUMMY_DEEP_LINK);

        mvc.perform(MockMvcRequestBuilders.post("/links/convert-to-url")
                .content(objectMapper.writeValueAsBytes(linkDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("url", is(equalTo(DUMMY_URL))))
                .andExpect(MockMvcResultMatchers.jsonPath("deepLink", is(equalTo(DUMMY_DEEP_LINK))));
    }
}
