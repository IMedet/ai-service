package kz.qonaqzhai.aiservice.service;

import kz.qonaqzhai.aiservice.dto.PlanFromTemplateRequest;
import kz.qonaqzhai.aiservice.dto.TemplateResponse;

import java.util.List;

public interface TemplateService {

    List<TemplateResponse> listTemplates();

    String buildPrompt(PlanFromTemplateRequest request);
}
