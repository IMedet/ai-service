package kz.qonaqzhai.aiservice.service;

import kz.qonaqzhai.aiservice.dto.AiPlanResponse;
import kz.qonaqzhai.aiservice.dto.EventPlan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiPlannerService {

    public AiPlanResponse plan(String prompt, String username) {
        AiPlanResponse response = new AiPlanResponse();
        response.setMessage("Plan generated. Review and book when ready.");
        response.setEventPlan(buildDemoPlan(prompt, username));
        return response;
    }

    private EventPlan buildDemoPlan(String prompt, String username) {
        EventPlan plan = new EventPlan();
        plan.setTimeline("Setup: 2 days before | Event day | Cleanup: 1 day after");

        List<EventPlan.Stage> stages = new ArrayList<>();

        EventPlan.Stage s1 = new EventPlan.Stage();
        s1.setId("stage-1");
        s1.setOrder(1);
        s1.setTitle("Stage 1: Foundation & Structure");
        s1.setDescription("Essential infrastructure setup - must be completed first");
        s1.setItems(List.of(
                item("1", "Event Tent (20x30m)", "Structure", 1, 850, "TentPro Almaty", null,
                        "Weather protection and event space definition"),
                item("2", "Flooring System", "Infrastructure", 600, 720, "EventFloors KZ", null,
                        "Level surface for furniture placement"),
                item("3", "Power Generator", "Utilities", 1, 380, "PowerRent Solutions", null,
                        "Electrical supply for all equipment")
        ));

        EventPlan.Stage s2 = new EventPlan.Stage();
        s2.setId("stage-2");
        s2.setOrder(2);
        s2.setTitle("Stage 2: Furniture & Seating");
        s2.setDescription("Requires Stage 1 completion - builds on infrastructure");
        s2.setItems(List.of(
                item("4", "Round Tables (10 seats)", "Furniture", 10, 350, "EventPro Almaty", "stage-1",
                        "Primary seating arrangement"),
                item("5", "Premium Chairs", "Furniture", 100, 400, "EventPro Almaty", "stage-1",
                        "Comfortable seating for all guests"),
                item("6", "VIP Lounge Furniture", "Premium Furniture", 1, 520, "Luxury Events KZ", "stage-1",
                        "Exclusive area for special guests")
        ));

        EventPlan.Stage s3 = new EventPlan.Stage();
        s3.setId("stage-3");
        s3.setOrder(3);
        s3.setTitle("Stage 3: Technical Setup");
        s3.setDescription("Audio/Visual equipment - requires power from Stage 1");
        s3.setItems(List.of(
                item("7", "Sound System Package", "Audio", 1, 620, "SoundWave Rentals", "stage-1",
                        "Clear audio for presentations and announcements"),
                item("8", "LED Stage Lighting", "Lighting", 1, 780, "LightUp Events", "stage-1",
                        "Professional ambiance and stage visibility"),
                item("9", "Projection Screen & Projector", "Visual", 1, 450, "AV Solutions KZ", "stage-3",
                        "Display presentations and multimedia content")
        ));

        EventPlan.Stage s4 = new EventPlan.Stage();
        s4.setId("stage-4");
        s4.setOrder(4);
        s4.setTitle("Stage 4: Decor & Finishing Touches");
        s4.setDescription("Final aesthetic elements - can be done alongside Stage 3");
        s4.setItems(List.of(
                item("10", "Floral Centerpieces", "Decoration", 10, 380, "Blossom Rentals", null,
                        "Elegant table decoration and atmosphere"),
                item("11", "Backdrop & Draping", "Decoration", 1, 420, "DecorMasters", null,
                        "Create focal point and professional look"),
                item("12", "Signage & Wayfinding", "Organization", 1, 180, "PrintPro Almaty", null,
                        "Help guests navigate the event space")
        ));

        stages.add(s1);
        stages.add(s2);
        stages.add(s3);
        stages.add(s4);

        plan.setStages(stages);
        plan.setTotalCost(stages.stream()
                .flatMap(s -> s.getItems().stream())
                .mapToDouble(EventPlan.Item::getCost)
                .sum());

        return plan;
    }

    private EventPlan.Item item(
            String id,
            String name,
            String category,
            int quantity,
            double cost,
            String supplier,
            String dependency,
            String reason
    ) {
        EventPlan.Item i = new EventPlan.Item();
        i.setId(id);
        i.setName(name);
        i.setCategory(category);
        i.setQuantity(quantity);
        i.setCost(cost);
        i.setSupplier(supplier);
        i.setDependency(dependency);
        i.setReason(reason);
        return i;
    }
}
