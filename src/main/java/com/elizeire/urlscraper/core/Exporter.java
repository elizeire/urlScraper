package com.elizeire.urlscraper.core;

import com.elizeire.urlscraper.domain.UrlEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to write response into desired format
 *
 * @author gbrasche
 * @since 12.2018
 */
public class Exporter {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Exporter() {
    }

    /**
     * Method to write into a file using comma separated fashion, CSV file
     *
     * @param urlEntityList the entity list to export to the file {@link UrlEntity}
     * @return the CSV File
     */
    public static File writeToCSVFile(List<UrlEntity> urlEntityList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Occurrences,");
        stringBuilder.append("Url" + "\n");

        urlEntityList.forEach(urlEntity -> stringBuilder.append(toCsvString(urlEntity)));

        File newFile = new File("result.csv");
        java.nio.file.Path path = Paths.get(newFile.getPath());
        byte[] strToBytes = stringBuilder.toString().getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error during multi thread processing ", e);
        }
        return newFile;
    }

    private static String toCsvString(UrlEntity urlEntity) {
        return urlEntity.getOccurrences() + "," + urlEntity.getUrl() + "\n";
    }

}
