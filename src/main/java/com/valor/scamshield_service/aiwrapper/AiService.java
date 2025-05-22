package com.valor.scamshield_service.aiwrapper;

import com.valor.scamshield_service.analyze.AnalyzeRequest;
import com.valor.scamshield_service.config.AiModelConfig;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

@Service
public class AiService {
    private final RestTemplate restTemplate;
    private final AiModelConfig aiModelConfig;

    public AiService(RestTemplate restTemplate, AiModelConfig aiModelConfig){
        this.restTemplate = restTemplate;
        this.aiModelConfig = aiModelConfig;
    }

    public AiResponse analyzeMessage(AnalyzeRequest request){
        AiRequest aiRequest = new AiRequest("gpt-4",request.getMessage());
        System.out.println("aiRequest: " + aiRequest);
        return postAi(aiRequest);
    }

    private AiResponse postAi(AiRequest aiRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + aiModelConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequest> requestEntity = new HttpEntity<>(aiRequest, headers);

        return restTemplate.exchange(
                aiModelConfig.getEndpoint(), // endpoint url
                HttpMethod.POST, // http method
                requestEntity, // body and headers
                AiResponse.class // class to deserialize response to
        ).getBody(); // extracts the response body from responseEntity
    }
}
