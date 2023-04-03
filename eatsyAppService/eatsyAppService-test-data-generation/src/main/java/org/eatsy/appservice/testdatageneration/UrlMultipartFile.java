package org.eatsy.appservice.testdatageneration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class UrlMultipartFile implements MultipartFile {

    private final byte[] imageData;
    private final String originalFilename;

    public UrlMultipartFile(final String imageUrl) throws IOException {
        // Download the image from the URL
        this.imageData = downloadImage(imageUrl);
        // Extract the filename from the URL
        this.originalFilename = extractFilename(imageUrl);
    }

    @Override
    public String getName() {
        return originalFilename;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return "image/jpeg";
    }

    @Override
    public boolean isEmpty() {
        return imageData == null || imageData.length == 0;
    }

    @Override
    public long getSize() {
        return imageData.length;
    }

    @Override
    public byte[] getBytes() {
        return imageData;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imageData);
    }

    @Override
    public void transferTo(final File dest) throws IOException, IllegalStateException {
        try (final OutputStream out = new FileOutputStream(dest)) {
            out.write(imageData);
        }
    }

    private byte[] downloadImage(final String imageUrl) throws IOException {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(imageUrl);
        final HttpResponse response = httpClient.execute(get);
        final HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toByteArray(entity);
        }
        return null;
    }

    private String extractFilename(final String imageUrl) {
        // Extract the filename from the URL or generate a unique filename
//        final int lastSlashIndex = imageUrl.lastIndexOf('/');
//        if (lastSlashIndex >= 0 && lastSlashIndex < imageUrl.length() - 1) {
//            return imageUrl.substring(lastSlashIndex + 1);
//        }
        return UUID.randomUUID() + ".jpg";
    }
}

