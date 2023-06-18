import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    
}

//TODO

//Client prepares an Authentication Request containing the desired request parameters.
//Client sends the request to the Authorization Server.
//Authorization Server Authenticates the End-User.
//Authorization Server obtains End-User Consent/Authorization.
//Authorization Server sends the End-User back to the Client with an ID Token and, if requested, an Access Token.
//Client validates the ID token and retrieves the End-User's Subject Identifier.
