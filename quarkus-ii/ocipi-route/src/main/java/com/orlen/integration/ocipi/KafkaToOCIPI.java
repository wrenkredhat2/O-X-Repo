package com.orlen.integration.ocipi;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;

public class KafkaToOCIPI extends RouteBuilder {
    @Override
    public void configure() throws Exception {

       from("kafka:{{kafka.topic}}?brokers={{kafka.bootstrap.servers}}" +
                  "&securityProtocol=SSL" +
                  "&sslTruststoreLocation={{kafka.ssl.truststore.location}}" +
                  "&sslTruststorePassword={{kafka.ssl.truststore.password}}")

        .routeId("ocipiroute")

       .log("Received message from Kafka: ${body}");

    }
}