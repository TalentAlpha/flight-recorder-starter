/*
 * Copyright 2020 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.flightrecorderstarter.configuration;

import com.google.cloud.storage.StorageOptions;
import de.mirkosertic.flightrecorderstarter.actuator.FlightRecorderEndpoint;
import de.mirkosertic.flightrecorderstarter.core.FlightRecorder;
import de.mirkosertic.flightrecorderstarter.storage.GoogleCloudStorageFacade;
import de.mirkosertic.flightrecorderstarter.storage.FlightRecordingUploader;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "flightrecorder", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@ImportAutoConfiguration(TriggerConfiguration.class)
@EnableConfigurationProperties({FlightRecorderDynamicConfiguration.class, StorageConfiguration.class})
public class FlightRecorderAutoConfiguration {

    @Bean
    public FlightRecorder flightRecorder(final FlightRecorderDynamicConfiguration configuration) {
        return new FlightRecorder(configuration);
    }

    @Bean
    public FlightRecorderEndpoint flightRecorderEndpoint(final FlightRecorder flightRecorder) {
        return new FlightRecorderEndpoint(flightRecorder);
    }

    @Bean
    public FlightRecordingUploader flightRecordingUploader(final StorageConfiguration storageConfiguration) {
        return new FlightRecordingUploader(new GoogleCloudStorageFacade(StorageOptions.getDefaultInstance().getService()), storageConfiguration);
    }
}
