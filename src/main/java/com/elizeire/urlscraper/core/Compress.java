package com.elizeire.urlscraper.core;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to bind to Gzip compression feature
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Compress {
}
