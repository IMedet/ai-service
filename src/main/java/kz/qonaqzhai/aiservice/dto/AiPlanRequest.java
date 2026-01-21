package kz.qonaqzhai.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AiPlanRequest {

    @NotBlank
    private String prompt;

    private String language; // "ru", "en", or "both" (default)

    private String eventType;

    private String location;

    private String eventDate; // ISO-8601 string

    private Integer guestCount;

    private Double budget;

    private List<CatalogItemHint> catalogItems;

    @Data
    public static class CatalogItemHint {
        @NotNull
        private Long id;
        @NotBlank
        private String name;
        private String category;
        private Double price;
        private String priceUnit;
        private String supplier;
        private Boolean available;
    }
}
