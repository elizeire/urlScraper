package com.elizeire.urlscraper.core;

import org.jsoup.nodes.Document;

import java.util.Collection;
import java.util.List;

/**
 * Interface to define global scraper behavior
 */
public interface Scraper {

    /**
     * Method to retrieve urls from all links at a web page
     *
     * @param url         the original url
     * @param elementName element to be be tracked, usually "a"
     * @return the list containing urls from the original website
     */
    Collection<String> getContentFromUrlByElementName(String url, String elementName);

    /**
     * Method to retrieve urls from all links at a web page
     *
     * @param document    representation of the content from the original url
     * @param elementName element to be be tracked, usually "a"
     * @return the list containing urls from the original website
     */
    List<String> processDocument(Document document, String elementName);

}
