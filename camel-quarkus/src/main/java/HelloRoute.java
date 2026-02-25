import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Configure the REST DSL
        restConfiguration()
            .component("platform-http")
            .bindingMode("off"); // We are just returning plain text

        // Define the GET endpoint
        rest("/hello")
            .get()
            .to("direct:sayHello");

        // The implementation route
        from("direct:sayHello")
            .transform().constant("Hello World");
    }
}