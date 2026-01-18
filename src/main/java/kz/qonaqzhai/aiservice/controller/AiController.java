package kz.qonaqzhai.aiservice.controller;

import jakarta.validation.Valid;
import kz.qonaqzhai.aiservice.dto.AiPlanRequest;
import kz.qonaqzhai.aiservice.dto.AiPlanResponse;
import kz.qonaqzhai.aiservice.service.AiPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiPlannerService aiPlannerService;

    @PostMapping("/plan")
    public ResponseEntity<AiPlanResponse> plan(@Valid @RequestBody AiPlanRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "anonymous";
        return ResponseEntity.ok(aiPlannerService.plan(request.getPrompt(), username));
    }
}
