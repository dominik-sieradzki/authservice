import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import authservice.AuthRequest;
import reactor.core.publisher.Mono;

public class WebClientService {
	private final WebClient webClient;
	private static final ObjectMapper mapper = new ObjectMapper();
	public WebClientService() {
        this.webClient = WebClient.create();
    }
	public AuthRequest callServerData(String apiURL) throws IOException {
        // Send the GET request and receive the response
		String url = apiURL;
        Mono<String> response = webClient.get()
	        .uri(url)
	        .accept(MediaType.APPLICATION_JSON)
	        .retrieve()
	        .bodyToMono(String.class).log();
        String json = response.block();
        return mapper.readValue(json, AuthRequest.class);
	}

}