package com.valor.scamshield_service.analyze;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private String summary;
    private String recommendation;
}
