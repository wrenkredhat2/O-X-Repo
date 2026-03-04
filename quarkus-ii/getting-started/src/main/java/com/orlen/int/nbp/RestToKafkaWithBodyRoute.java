import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;

public class RestToKafkaWithBodyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // Define the receiving endpoint
        rest("/trigger")
            .get()
            .to("direct:processRequest");

        from("direct:processRequest")
        .log ("Hello From Route")

            .routeId("BodyTransferRoute")
                        
            // 1. Prepare for the outgoing call
            // We set the HTTP method for the external service
            .setHeader(Exchange.HTTP_METHOD, constant("GET"))
//            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/xml"))
            .removeHeader(Exchange.HTTP_PATH)

            // 2. Call the external service
            // The body received in the 'rest' call is automatically sent as the payload
            .to("https://api.nbp.pl/api/exchangerates/tables/A?bridgeEndpoint=true")
            
            // 3. Transform the service's response to JSON (if it isn't already)
            .marshal().json(JsonLibrary.Jackson)
            
            // 4. Send the result to Kafka
//            .to("kafka:processed-data?brokers={{kafka.bootstrap.servers}}" +
//                "&securityProtocol={{kafka.security.protocol}}" +
//                "&sslTruststoreLocation={{kafka.ssl.truststore.location}}" +
//                "&sslTruststorePassword={{kafka.ssl.truststore.password}}")
            
            // 5. Optional: Return a success message to the original caller
            .setBody(constant("{\"status\": \"Message processed and sent to Kafka\"}"));
    }
}