package kz.qonaqzhai.aiservice.controller;

import jakarta.validation.Valid;
import kz.qonaqzhai.aiservice.dto.AiPlanRequest;
import kz.qonaqzhai.aiservice.dto.AiPlanResponse;
import kz.qonaqzhai.aiservice.dto.PlanFromTemplateRequest;
import kz.qonaqzhai.aiservice.dto.TemplateResponse;
import kz.qonaqzhai.aiservice.service.AiPlannerService;
import kz.qonaqzhai.aiservice.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiPlannerService aiPlannerService;

    private final TemplateService templateService;

    @PostMapping("/plan")
    public ResponseEntity<AiPlanResponse> plan(@Valid @RequestBody AiPlanRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "anonymous";
        return ResponseEntity.ok(aiPlannerService.plan(request, username));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<TemplateResponse>> templates() {
        return ResponseEntity.ok(templateService.listTemplates());
    }

    @PostMapping("/plan-from-template")
    public ResponseEntity<AiPlanResponse> planFromTemplate(@Valid @RequestBody PlanFromTemplateRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "anonymous";

        AiPlanRequest aiReq = new AiPlanRequest();
        aiReq.setPrompt(templateService.buildPrompt(request));
        aiReq.setLanguage(request.getLanguage());
        aiReq.setLocation(request.getLocation());
        aiReq.setGuestCount(request.getGuestCount());
        aiReq.setBudget(request.getBudget());

        return ResponseEntity.ok(aiPlannerService.plan(aiReq, username));
    }
}
