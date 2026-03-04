import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;

public class RestToKafkaWithBodyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // Define the receiving endpoint
        rest("/trigger")
            .post() // Expecting a POST request with a body
            .to("direct:processRequest");

        from("direct:processRequest")
            .routeId("BodyTransferRoute")
            
            // 1. Prepare for the outgoing call
            // We set the HTTP method for the external service
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            
            // 2. Call the external service
            // The body received in the 'rest' call is automatically sent as the payload
            .to("http://api.externalservice.com/v1/ingest")
            
            // 3. Transform the service's response to JSON (if it isn't already)
            .marshal().json(JsonLibrary.Jackson)
            
            // 4. Send the result to Kafka
            .to("kafka:processed-data?brokers=localhost:9092")
            
            // 5. Optional: Return a success message to the original caller
            .setBody(constant("{\"status\": \"Message processed and sent to Kafka\"}"));
    }
}