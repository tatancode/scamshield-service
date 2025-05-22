package com.valor.scamshield_service.aiwrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiResponse {
    String id;
    String object;
    Long created_at;
    String status;
    String error;
    String model;
    List<Output> output;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Output {
    private String type;
    private String id;
    private String status;
    private String role;
    private List<Content> content;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Content {
    private String type;
    private String text;
    private List<String> annotations;
}
