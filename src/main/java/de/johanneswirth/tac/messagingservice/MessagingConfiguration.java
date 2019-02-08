package de.johanneswirth.tac.messagingservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.discovery.DiscoveryFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MessagingConfiguration extends Configuration {

    @Valid
    @NotNull
    private DiscoveryFactory discovery = new DiscoveryFactory();

    @JsonProperty("discovery")
    public DiscoveryFactory getDiscoveryFactory() {
        return discovery;
    }

    @JsonProperty("discovery")
    public void setDiscoveryFactory(DiscoveryFactory discoveryFactory) {
        this.discovery = discoveryFactory;
    }
}