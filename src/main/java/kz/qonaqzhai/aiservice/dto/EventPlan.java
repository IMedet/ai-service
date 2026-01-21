package kz.qonaqzhai.aiservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventPlan {

    private List<Stage> stages;
    private double totalCost;
    private String timeline;

    @Data
    public static class Stage {
        private String id;
        private String title;
        private String description;
        private int order;
        private List<Item> items;
    }

    @Data
    public static class Item {
        private String id;
        private Long catalogItemId;
        private String name;
        private String category;
        private int quantity;
        private double cost;
        private String supplier;
        private String dependency;
        private String reason;
    }
}
