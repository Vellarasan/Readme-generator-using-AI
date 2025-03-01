package io.vels.readme.generator.service;

import io.vels.readme.generator.utils.AiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiClientService {

    private static final Logger log = LoggerFactory.getLogger(AiClientService.class);
    private final AiUtils aiUtils;
    private final TokenizerService tokenizerService;
    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    public AiClientService(AiUtils aiUtils, TokenizerService tokenizerService) {
        this.aiUtils = aiUtils;
        this.tokenizerService = tokenizerService;
    }

    public String generatePrompt(String promptTemplateUri, String... args) {

        // Constructing Prompt with values
        String rawPrompt = aiUtils.constructPrompt(promptTemplateUri, args);

        // Tokenize the Constructed Prompt
        String sanitizedPrompt = tokenizerService.processForModel(rawPrompt, model, 3000);

        // Logging the saved token details
        logSavedTokens(rawPrompt, sanitizedPrompt);

        // Invoking the AI Model
        return aiUtils.invokeWithPromptAndValues(sanitizedPrompt);
    }

    private static void logSavedTokens(String rawPrompt, String tokenizedPrompt) {
        int rawTokensCount = rawPrompt.toCharArray().length;
        int sanitizedTokensCount = tokenizedPrompt.toCharArray().length;
        log.info("Raw Prompt's Token Size ::: {}", rawTokensCount);
        log.info("Sanitized Prompt Token Size ::: {}", sanitizedTokensCount);
        log.info("Tokens Saved ::: {} ", rawTokensCount - sanitizedTokensCount);
    }

}
