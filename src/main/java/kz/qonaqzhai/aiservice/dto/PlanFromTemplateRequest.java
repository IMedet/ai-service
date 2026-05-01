package kz.qonaqzhai.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlanFromTemplateRequest {

    @NotBlank
    private String templateId;

    private String language; // "ru", "en", or "both"

    private String location;

    private Integer guestCount;

    private Double budget;
}
