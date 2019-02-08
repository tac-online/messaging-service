package de.johanneswirth.tac.messagingservice;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.client.DiscoveryClient;
import io.dropwizard.discovery.client.DiscoveryClientManager;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;

import javax.ws.rs.client.Client;

public class MessagingServiceApp extends Application<MessagingConfiguration> {

    private final DiscoveryBundle<MessagingConfiguration> discoveryBundle = new DiscoveryBundle<MessagingConfiguration>() {
        @Override
        public DiscoveryFactory getDiscoveryFactory(MessagingConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }

    };

    public static void main(String[] args) throws Exception {
        new MessagingServiceApp().run(args);
    }

    public void initialize(Bootstrap<MessagingConfiguration> bootstrap) {
        bootstrap.addBundle(new WebsocketBundle(Websocket.class));
        bootstrap.addBundle(discoveryBundle);
    }

    @Override
    public void run(MessagingConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(MessagingService.class);
        final DiscoveryClient discoveryClient = discoveryBundle.newDiscoveryClient("other-service");
        environment.lifecycle().manage(new DiscoveryClientManager(discoveryClient));
    }
}
