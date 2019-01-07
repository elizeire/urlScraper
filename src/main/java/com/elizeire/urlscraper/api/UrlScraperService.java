package com.elizeire.urlscraper.api;

import com.elizeire.urlscraper.core.Compress;
import com.elizeire.urlscraper.core.Exporter;
import com.elizeire.urlscraper.business.UrlScraperBusiness;
import com.elizeire.urlscraper.domain.UrlEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

/**
 * Class to expose REST services
 * @author gbrasche
 * @since 12.2018
 */
@ApplicationScoped
@Path("/")
public class UrlScraperService {

    @Inject
    private UrlScraperBusiness urlScraperBusiness;

    @GET
    @Path("from={url}")
    @Compress
    @Produces({MediaType.APPLICATION_JSON})
    public List<UrlEntity> getUrls(@PathParam("url") String urlFrom) {

        return urlScraperBusiness.processURL(urlFrom);
    }

    @GET
    @Path("from={url}/csv")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("url") String urlFrom) {

        File csvFile = Exporter.writeToCSVFile(urlScraperBusiness.processURL(urlFrom));

        return Response.ok(csvFile).header("Content-Disposition", "attachment;filename=result.csv").build();
    }
}
