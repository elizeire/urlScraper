package com.elizeire.urlscraper.api;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UrlScraperServiceIT {

    private Client client;

    private static String BASE_URL = "http://localhost:8080/";
    private static String JSON_REQUEST_URL = "urlScraper/from=https:%2F%2Fen.wikipedia.org%2Fwiki%2FEurope";
    private static String CSV_REQUEST_URL = "urlScraper/from=https:%2F%2Fen.wikipedia.org%2Fwiki%2FEurope/csv";


    @BeforeEach
    public void setup() {
        client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);
    }

    @AfterEach
    public void teardown() {
        client.close();
    }


    @Test
    void getUrls() {
        Response response = this.getResponse(BASE_URL + JSON_REQUEST_URL);
        this.assertResponse(response);

        JsonArray entities = response.readEntity(JsonArray.class);

        int expected = 372800;
        int actual = entities.size();

        response.close();

        assertTrue(expected < actual);
    }

    @Test
    void downloadFile() throws IOException {

        Response response = this.getResponse(BASE_URL + CSV_REQUEST_URL);
        this.assertResponse(response);

        InputStream is = (InputStream)response.getEntity();

        byte[] byteArray = IOUtils.readBytesFromStream(is);
        File file = new File("UrlScraperServiceTest_downloadFile_response.csv");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(byteArray);
        fos.flush();
        fos.close();

        double expected = 20; // asserting if file has 20mb in total size
        double actual = (file.length() / 1024 / 1024);

        response.close();
        assertEquals(expected , actual);
    }


    private Response getResponse(String url) {
        return client.target(url).register(GZIPFeature.class).request().get();
    }

    private void assertResponse(Response response) {
        assertEquals(200, response.getStatus());
    }
}