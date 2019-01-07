package com.elizeire.urlscraper.core;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Class to provide Gzip compression to REST responses
 * @author gbrasche
 * @since 12.2018
 */
@Provider
@Compress
public class GZIPWriterInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException {
        context.getHeaders().add("Content-Encoding", "gzip");
        context.setOutputStream(new GZIPOutputStream(context.getOutputStream()));
        context.proceed();
    }
}
