package com.elizeire.urlscraper.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

/**
 * Class to implement a Web-Scraper specific for wikipedia sites
 */
@ApplicationScoped
public class ScraperWikiImpl implements Scraper {


    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);

    @Override
    public Collection<String> getContentFromUrlByElementName(String url, String elementName) {
        List<String> urls = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            urls = processDocument(document, elementName);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error during url processing: " + e.getMessage());
        }
        return urls;
    }

    public List<String> processDocument(Document document, String elementName) {
        List<String> urls = new ArrayList<>();
        Elements elements = document.select(elementName);
        elements.forEach(e -> processElement(e, urls));
        return urls;
    }


    private void processElement(Element e, List<String> urls) {
        String url = resolveUrl(e);
        if (url != null) {
            urls.add(url);
        }
    }

    private String resolveUrl(Element e) {

        String url = null;

        if (e.attr("href").startsWith("/wiki/")) {
            url = e.attr("href");
            if (url.startsWith("/")) {
                url = "https://en.wikipedia.org" + url;
            }
            if (url.startsWith("#")) {
                url = null;
            }
        }
        return url;
    }
}