package kz.qonaqzhai.aiservice.dto;

import lombok.Data;

@Data
public class AiPlanResponse {

    private String message;
    private EventPlan eventPlan;
}
