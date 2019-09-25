package de.johanneswirth.tac.messagingservice;

import de.johanneswirth.tac.common.Utils;
import io.dropwizard.Application;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;

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
        Utils.init(configuration.getPublicKey());
        environment.jersey().register(MessagingService.class);
    }
}
