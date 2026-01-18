package kz.qonaqzhai.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiPlanRequest {

    @NotBlank
    private String prompt;
}
