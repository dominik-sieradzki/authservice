package authservice;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import reactor.core.publisher.Mono;

@RestController
@Service
@Transactional
public class AuthRequestController {
	private WebClient webClient;
	List<String> acceptedClients;
	List<String> redirectURIs;
	List<String> responseTypes;

	@GetMapping(value = "/authorize")
    void authorization(@Valid @RequestBody AuthRequest o) {
        if(!acceptedClients.contains(o.getClient_id())) {
        	wrongClient();
        }
        else if (!redirectURIs.contains(o.getRedirect_uri())) {
        	wrongRedirect();
        }
        else if (!responseTypes.contains(o.getResponse_type())) {
        	wrongType();
        }
        else if(!o.getScope().contains("openid")) {
        	wrongScope();
        }
        else ok(o);
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "invalid client_id")
	void wrongClient() {
		
	}
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "URL not registered")
	void wrongRedirect() {
		
	}
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "response type not registered")
	void wrongType() {
		
	}
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "scope does not include 'openid'")
	void wrongScope() {
		
	}
	@ResponseStatus(value=HttpStatus.OK, reason = "OK")
	void ok(AuthRequest o) {
		MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
		if(o.getResponse_type() != "id_token") {
			bodyValues.add("access_token", generateToken(20));
			bodyValues.add("token_type", "Bearer");	
		}
		
		bodyValues.add("id_token", o.getClient_id());
		bodyValues.add("state", o.getState());
		bodyValues.add("expires_in", "600");//10 minutes
		
		String response = webClient.post()
		        .uri(o.getRedirect_uri())
		        .accept(MediaType.APPLICATION_JSON)
		        .body(BodyInserters.fromFormData(bodyValues))
		        .retrieve()
		        .bodyToMono(String.class).log()
				.block();
	}
	
	public String generateToken(int length) {
		byte[] array = new byte[length]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	    return generatedString;
	}

}
