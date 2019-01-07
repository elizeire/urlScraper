package com.elizeire.urlscraper.core;

import com.elizeire.urlscraper.business.UrlScraperBusiness;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class UrlScraperBusinessTest {

    private final UrlScraperBusiness urlScraperBusiness = new UrlScraperBusiness();

    @Test
    void retrieveUrlsAndCounter() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("\\testData\\example.html");

        Document document = Jsoup.parse(inputStream, "UTF-8", "https://en.wikipedia.org/wiki/European_Union");

        Scraper scraper = new ScraperWikiImpl();
        List<String> content = scraper.processDocument(document, "a");

        Map<String, Long> urlsAndCounter = urlScraperBusiness.retrieveUrlsAndCounter(content);

        Assertions.assertEquals(1L, (long) urlsAndCounter.get("https://en.wikipedia.org/wiki/link1.html"));
        Assertions.assertEquals(2L, (long) urlsAndCounter.get("https://en.wikipedia.org/wiki/link2.html"));
        Assertions.assertEquals(3L, (long) urlsAndCounter.get("https://en.wikipedia.org/wiki/link3.html"));
        Assertions.assertEquals(4L, (long) urlsAndCounter.get("https://en.wikipedia.org/wiki/link4.html"));

    }
}