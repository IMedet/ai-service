package kz.qonaqzhai.aiservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class TemplateResponse {

    private String id;
    private String title;
    private String description;
    private String guestRange;
    private String budgetRange;
    private String duration;
    private List<String> previewItems;
}
