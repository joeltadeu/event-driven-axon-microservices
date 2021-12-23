package com.eventdrivenmicroservices.productservice.command.rest;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/management")
public class EventReplayController {

    private final EventProcessingConfiguration configuration;

    public EventReplayController(EventProcessingConfiguration configuration) {
        this.configuration = configuration;
    }

    @PostMapping("eventProcessor/{processorName}/reset")
    public ResponseEntity<String> replayEvents(@PathVariable String processorName) {
        Optional<TrackingEventProcessor> trackingEventProcessor = configuration.eventProcessor(processorName,
            TrackingEventProcessor.class);

        if (trackingEventProcessor.isPresent()) {
            TrackingEventProcessor eventProcessor = trackingEventProcessor.get();
            eventProcessor.shutDown();
            eventProcessor.resetTokens();
            eventProcessor.start();

            return ResponseEntity.ok().body(String.format("The event processor with a name [%s] has been reset",
                processorName));
        } else {
            return ResponseEntity.badRequest().body(String.format("The event processor with a name [%s] is not a " +
                                                                  "tracking event processor." + " Only Tracking event" +
                                                                  " processor is supported", processorName));
        }
    }
}
