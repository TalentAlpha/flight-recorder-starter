package de.mirkosertic.flightrecorderstarter.storage;

import de.mirkosertic.flightrecorderstarter.configuration.StorageConfiguration;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class FlightRecordingUploader {

    private static final String FLIGHT_RECORDING_FILE_TYPE = ".jfr";

    private final GoogleCloudStorageFacade googleCloudStorageFacade;

    private final StorageConfiguration storageConfiguration;

    public FlightRecordingUploader(GoogleCloudStorageFacade googleCloudStorageFacade, StorageConfiguration storageConfiguration) {
        this.googleCloudStorageFacade = googleCloudStorageFacade;
        this.storageConfiguration = storageConfiguration;
    }

    public void uploadRecording(InputStream recordingInputStream) {
        final var key = ZonedDateTime.now(ZoneOffset.UTC).toString() + '_' + UUID.randomUUID().toString() + FLIGHT_RECORDING_FILE_TYPE;
        googleCloudStorageFacade.uploadObject(storageConfiguration.getBucket(), key, recordingInputStream,
                                              MediaType.APPLICATION_OCTET_STREAM.getType());
    }

}
