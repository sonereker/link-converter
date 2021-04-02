package com.sonereker.linkconverter.link;

import com.sonereker.linkconverter.page.PageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;

/**
 * REST service responsible for the link operations
 */
@RestController
@RequestMapping("links")
public class LinkConversionController {

    private final PageServiceImpl pageService;
    private final LinkConversionRepository linkConversionRepository;

    @Autowired
    public LinkConversionController(PageServiceImpl pageService, LinkConversionRepository linkConversionRepository) {
        this.pageService = pageService;
        this.linkConversionRepository = linkConversionRepository;
    }

    /**
     * Converts given url to deepLink
     *
     * @param link {@link com.sonereker.linkconverter.link.LinkDto} object with a non-empty <code>deepLink</code> value.
     * @return {@link com.sonereker.linkconverter.link.LinkDto} object having both <code>url</code> and <code>deepLink</code> fields set.
     * @throws MalformedURLException is thrown when URL is not parsable
     */
    @PostMapping("convert-to-deeplink")
    public ResponseEntity<Object> convertToDeepLink(@RequestBody LinkDto link) throws MalformedURLException {
        if (link.url() == null || link.url().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'url' should be provided.");
        }

        var pageType = pageService.findPageTypeByUrl(link.url());
        var deepLink = pageType.generateDeepLinkFromUrl(link.url());
        var linkDto = new LinkDto(link.url(), deepLink);

        linkConversionRepository.save(LinkConversion.fromDto(linkDto));

        return new ResponseEntity<>(linkDto, HttpStatus.OK);
    }

    /**
     * Converts given deepLink to url
     *
     * @param link {@link com.sonereker.linkconverter.link.LinkDto} object with a non-empty <code>url</code> value.
     * @return {@link com.sonereker.linkconverter.link.LinkDto} object having both <code>url</code> and <code>deepLink</code> fields set.
     */
    @PostMapping("convert-to-url")
    public ResponseEntity<Object> convertToUrl(@RequestBody LinkDto link) {
        if (link.deepLink() == null || link.deepLink().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'deepLink' should be provided.");
        }

        var pageType = pageService.findPageTypeByDeepLink(link.deepLink());
        var url = pageType.generateUrlFromDeepLink(link.deepLink());
        var responseDto = new LinkDto(url, link.deepLink());

        linkConversionRepository.save(LinkConversion.fromDto(responseDto));

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
