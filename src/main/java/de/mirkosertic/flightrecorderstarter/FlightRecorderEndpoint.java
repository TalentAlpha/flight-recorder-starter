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
package de.mirkosertic.flightrecorderstarter;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerEndpoint(id="flightrecorder")
public class FlightRecorderEndpoint {

    private final static Logger LOGGER = Logger.getLogger(FlightRecorder.class.getCanonicalName());

    public static class StartRecordingCommand {
        private long duration;
        private String timeUnit;

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(String timeUnit) {
            this.timeUnit = timeUnit;
        }
    }

    private final FlightRecorder flightRecorder;

    public FlightRecorderEndpoint(FlightRecorder flightRecorder) {
        this.flightRecorder = flightRecorder;
    }

    @PutMapping("/")
    public @ResponseBody ResponseEntity startRecording(@RequestBody final StartRecordingCommand command) {
        try {
            LOGGER.log(Level.INFO, "Trying to start recording for {0} {1}", new Object[] {command.duration, command.timeUnit});
            final long recordingId = flightRecorder.startRecordingFor(Duration.of(command.duration, ChronoUnit.valueOf(command.timeUnit)));
            LOGGER.log(Level.INFO, "Created recording with ID {0}", recordingId);
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(Long.toString(recordingId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{recordingId}")
    public @ResponseBody ResponseEntity closeRecording(@PathVariable final long recordingId) {
        try {
            LOGGER.log(Level.INFO, "Closing recording with ID {0}", recordingId);
            flightRecorder.stopRecording(recordingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{recordingId}")
    public @ResponseBody ResponseEntity downloadRecording(@PathVariable final long recordingId) {
        LOGGER.log(Level.INFO, "Closing recording with ID {0} and downloading file", recordingId);
        final File file = flightRecorder.stopRecording(recordingId);
        if (file != null) {
            final HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=flightrecording_" + recordingId + ".jfr");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(file));
        }
        return ResponseEntity.notFound().build();
    }
}