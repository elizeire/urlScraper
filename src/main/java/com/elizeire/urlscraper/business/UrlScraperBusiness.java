package com.elizeire.urlscraper.business;

import com.elizeire.urlscraper.core.Scraper;
import com.elizeire.urlscraper.domain.UrlEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class to process the requests in according with business requirements
 *
 * @author gbrasche
 * @since 12.2018
 */
@ApplicationScoped
public class UrlScraperBusiness {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Inject
    private Scraper scraper;

    /**
     * Method responsible to retrieve all urls from all links from a specific website
     * extract and provide a List of {@link UrlEntity}
     *
     * @param url the original website
     * @return the list of {@link UrlEntity}
     */
    public List<UrlEntity> processURL(String url) {
        Collection<String> content = getContent(url);

        Collection<String> contentChildren = getContentChildren(content);

        content.addAll(contentChildren);

        Map<String, Long> urlsAndCounter = retrieveUrlsAndCounter(content);
        return createEntities(urlsAndCounter);
    }

    private Collection<String> getContent(String originalUrl) {
        return scraper.getContentFromUrlByElementName(originalUrl, "a");
    }

    private Collection<String> getContentChildren(Collection<String> childrenList) {

        Collection<String> result = new ArrayList<>();
        Map<Future, String> tasks = new LinkedHashMap<>();

        //here we should avoid duplicates
        Set<String> uniqueUrls = new HashSet<>(childrenList);

        ExecutorService executorService = Executors.newFixedThreadPool(50);

        for (String childUrl : uniqueUrls) {
            Callable callable = () -> scraper.getContentFromUrlByElementName(childUrl, "a");

            Future future = executorService.submit(callable);
            tasks.put(future, childUrl);
        }
        tasks.forEach((future, url) -> {
            try {
                Collection<String>
                        content = (Collection<String>) future.get(120, TimeUnit.SECONDS);
                (result).addAll(content);
            } catch (InterruptedException | ExecutionException
                    | TimeoutException e) {
                LOGGER.log(Level.WARNING, "Error during multi thread processing ", e);
            }
        });
        executorService.shutdown();
        return result;
    }

    /**
     * Method responsible to aggregate all results and count repeated occurrences
     *
     * @param content a list of urls to be aggregated and counted
     * @return a map where the key is the url and the value the number of occurrences
     */
    public Map<String, Long> retrieveUrlsAndCounter(Collection<String> content) {
        return content.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    private List<UrlEntity> createEntities(Map<String, Long> urlsAndCounterMap) {
        List<UrlEntity> entities = new ArrayList<>();
        urlsAndCounterMap.forEach((s, aLong) -> {
            UrlEntity urlEntity = new UrlEntity();
            urlEntity.setOccurrences(aLong);
            urlEntity.setUrl(s);
            entities.add(urlEntity);
        });
        return entities;
    }

}
