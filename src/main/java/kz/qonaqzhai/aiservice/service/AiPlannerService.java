package kz.qonaqzhai.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.qonaqzhai.aiservice.dto.AiPlanRequest;
import kz.qonaqzhai.aiservice.dto.AiPlanResponse;
import kz.qonaqzhai.aiservice.dto.EventPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiPlannerService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.google.genai.api-key:}")
    private String apiKey;

    public AiPlanResponse plan(AiPlanRequest request, String username) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Google AI Studio API key is not configured (GOOGLE_AI_STUDIO_API_KEY)");
        }

        String language = request.getLanguage();
        if (language == null || language.isBlank()) {
            language = "both";
        }

        String system = """
You are Qonaqzhai, an expert event planning assistant for a premium rental service in Kazakhstan.
Your personality: warm, enthusiastic, detail-oriented, and genuinely helpful — like a professional event planner.

GUIDELINES:
- Respond in a natural, conversational tone. Write like a human event planner talking to a client.
- When the user mentions a specific event (wedding, corporate, birthday), get excited and engage with it.
- For Kazakh locations, mention local context when relevant (Almaty seasons, Astana venues, traditions).
- Give practical, actionable advice — not generic fluff. Be specific about quantities and setups.
- Structure the eventPlan with realistic stages (3-5 stages), real item names, and KZT-appropriate pricing.
- Kazakhstan typical prices (KZT): tents 50,000-200,000, chairs 500-2,000 each, sound systems 30,000-80,000, catering 3,000-8,000 per person, decoration packages 20,000-100,000.
- If the user's prompt is vague, ask 1-2 clarifying questions in the message before suggesting a plan.
- Write messageRu in natural Russian, messageEn in natural English. They should each feel independently written, not just translations of each other.
- Keep messages warm and concise (4-8 sentences each). End with a helpful summary or next step.
- NEVER use markdown formatting in messageRu/messageEn — they are displayed as plain text.

OUTPUT: Return ONLY valid JSON matching the schema below. No markdown fences, no extra commentary.
""";

        String schema = "{\n" +
                "  \"messageRu\": string,\n" +
                "  \"messageEn\": string,\n" +
                "  \"eventPlan\": {\n" +
                "    \"timeline\": string,\n" +
                "    \"totalCost\": number,\n" +
                "    \"stages\": [\n" +
                "      {\n" +
                "        \"id\": string,\n" +
                "        \"order\": number,\n" +
                "        \"title\": string,\n" +
                "        \"description\": string,\n" +
                "        \"items\": [\n" +
                "          {\n" +
                "            \"id\": string,\n" +
                "            \"catalogItemId\": number|null,\n" +
                "            \"name\": string,\n" +
                "            \"category\": string,\n" +
                "            \"quantity\": number,\n" +
                "            \"cost\": number,\n" +
                "            \"supplier\": string,\n" +
                "            \"dependency\": string|null,\n" +
                "            \"reason\": string\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        String userPrompt = buildUserPrompt(request, username, language, schema);

        ChatClient client = chatClientBuilder.build();
        ChatResponse chatResponse = client.prompt()
                .system(system)
                .user(userPrompt)
                .call()
                .chatResponse();

        String content = chatResponse.getResult().getOutput().getText();
        try {
            return objectMapper.readValue(content, AiPlanResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse AI response as JSON", e);
        }
    }

    private String buildUserPrompt(AiPlanRequest request, String username, String language, String schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(username).append("\n");
        sb.append("Language: ").append(language).append("\n");

        if (request.getEventType() != null) sb.append("EventType: ").append(request.getEventType()).append("\n");
        if (request.getLocation() != null) sb.append("Location: ").append(request.getLocation()).append("\n");
        if (request.getEventDate() != null) sb.append("EventDate: ").append(request.getEventDate()).append("\n");
        if (request.getGuestCount() != null) sb.append("GuestCount: ").append(request.getGuestCount()).append("\n");
        if (request.getBudget() != null) sb.append("Budget: ").append(request.getBudget()).append("\n");

        sb.append("\nFree-form prompt:\n").append(request.getPrompt()).append("\n\n");

        if (request.getCatalogItems() != null && !request.getCatalogItems().isEmpty()) {
            sb.append("Catalog items available (use catalogItemId when you select an item):\n");
            for (AiPlanRequest.CatalogItemHint item : request.getCatalogItems()) {
                sb.append("- id=").append(item.getId())
                        .append(" name=").append(item.getName());
                if (item.getCategory() != null) sb.append(" category=").append(item.getCategory());
                if (item.getPrice() != null) sb.append(" price=").append(item.getPrice());
                if (item.getPriceUnit() != null) sb.append(" unit=").append(item.getPriceUnit());
                if (item.getSupplier() != null) sb.append(" supplier=").append(item.getSupplier());
                if (item.getAvailable() != null) sb.append(" available=").append(item.getAvailable());
                sb.append("\n");
            }
            sb.append("\n");
        }

        if (request.getConversationHistory() != null && !request.getConversationHistory().isEmpty()) {
            sb.append("Previous conversation:\n");
            for (AiPlanRequest.ConversationMessage msg : request.getConversationHistory()) {
                sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
            }
            sb.append("\n");
        }

        sb.append("Return JSON strictly matching this schema:\n");
        sb.append(schema);
        return sb.toString();
    }
}
