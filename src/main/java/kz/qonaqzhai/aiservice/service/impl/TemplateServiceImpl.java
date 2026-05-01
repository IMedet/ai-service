package kz.qonaqzhai.aiservice.service.impl;

import kz.qonaqzhai.aiservice.dto.PlanFromTemplateRequest;
import kz.qonaqzhai.aiservice.dto.TemplateResponse;
import kz.qonaqzhai.aiservice.service.TemplateService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Override
    public List<TemplateResponse> listTemplates() {
        List<TemplateResponse> res = new ArrayList<>();

        res.add(template(
                "wedding",
                "Wedding Reception",
                "Elegant wedding setup with complete decoration, seating, and technical equipment",
                "50-300 guests",
                "$5,000 - $15,000",
                "8-12 hours",
                List.of("Premium seating", "Floral decorations", "Professional lighting", "Sound system")
        ));

        res.add(template(
                "corporate",
                "Corporate Conference",
                "Professional conference setup with AV equipment, networking spaces, and catering",
                "50-500 guests",
                "$3,000 - $20,000",
                "1-3 days",
                List.of("Stage & podium", "Projection systems", "Registration desks", "Networking lounges")
        ));

        res.add(template(
                "birthday",
                "Birthday Party",
                "Fun and festive birthday celebration with entertainment, decor, and catering setup",
                "20-100 guests",
                "$1,000 - $5,000",
                "4-6 hours",
                List.of("Themed decorations", "Entertainment setup", "Catering equipment", "Photo booth")
        ));

        res.add(template(
                "graduation",
                "Graduation Ceremony",
                "Formal graduation event with stage, seating, and professional photo opportunities",
                "100-1000 guests",
                "$2,000 - $10,000",
                "3-5 hours",
                List.of("Stage platform", "Graduate seating", "Photo backdrops", "Sound amplification")
        ));

        res.add(template(
                "networking",
                "Networking Event",
                "Casual networking mixer with lounge seating, bars, and ambient atmosphere",
                "30-200 guests",
                "$1,500 - $8,000",
                "3-5 hours",
                List.of("Cocktail tables", "Bar setup", "Ambient lighting", "Background music")
        ));

        return res;
    }

    @Override
    public String buildPrompt(PlanFromTemplateRequest request) {
        String id = request.getTemplateId();
        if (id == null) {
            throw new IllegalArgumentException("templateId is required");
        }
        id = id.trim().toLowerCase(Locale.ROOT);

        String base;
        switch (id) {
            case "wedding" -> base = "Plan a wedding reception.";
            case "corporate" -> base = "Plan a corporate conference.";
            case "birthday" -> base = "Plan a birthday party.";
            case "graduation" -> base = "Plan a graduation ceremony.";
            case "networking" -> base = "Plan a networking event.";
            default -> throw new IllegalArgumentException("Unknown templateId: " + id);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(base);

        if (request.getGuestCount() != null) {
            sb.append(" Guest count: ").append(request.getGuestCount()).append('.');
        }
        if (request.getBudget() != null) {
            sb.append(" Budget: ").append(request.getBudget()).append('.');
        }
        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            sb.append(" Location: ").append(request.getLocation()).append('.');
        }

        sb.append(" Provide a complete multi-stage rental plan with quantities and realistic costs.");
        return sb.toString();
    }

    private static TemplateResponse template(
            String id,
            String title,
            String description,
            String guestRange,
            String budgetRange,
            String duration,
            List<String> previewItems
    ) {
        TemplateResponse t = new TemplateResponse();
        t.setId(id);
        t.setTitle(title);
        t.setDescription(description);
        t.setGuestRange(guestRange);
        t.setBudgetRange(budgetRange);
        t.setDuration(duration);
        t.setPreviewItems(previewItems);
        return t;
    }
}
