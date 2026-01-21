package kz.qonaqzhai.aiservice.dto;

import lombok.Data;

@Data
public class AiPlanResponse {

    private String messageRu;
    private String messageEn;
    private EventPlan eventPlan;
}
