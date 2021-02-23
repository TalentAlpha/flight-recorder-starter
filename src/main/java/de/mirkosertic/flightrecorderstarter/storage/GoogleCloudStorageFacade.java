package de.mirkosertic.flightrecorderstarter.storage;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.IOException;
import java.io.InputStream;

public class GoogleCloudStorageFacade {

    private final Storage storage;

    public GoogleCloudStorageFacade(Storage storage) {
        this.storage = storage;
    }

    public void uploadObject(String bucket, String key, InputStream inputStream, String contentType) {
        try {
            storage.createFrom(BlobInfo.newBuilder(bucket, key)
                                       .setContentType(contentType)
                                       .build(),
                               inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Recording upload unsuccessful: " + bucket + "_" + key);
        }
    }

}
