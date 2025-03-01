package io.vels.readme.generator.service;

import com.knuddels.jtokkit.api.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Advanced tokenizer service that uses an actual tokenizer library (JTokkit)
 * for more accurate token counting and optimization.
 */
@Service
public class TokenizerService {

    private final EncodingRegistry registry;
    private final TokenSanitizerService sanitizerService;

    public TokenizerService(EncodingRegistry registry, TokenSanitizerService sanitizerService) {
        this.registry = registry;
        this.sanitizerService = sanitizerService;
    }

    /**
     * Process text with actual token-aware trimming for specific AI model
     *
     * @param text      Input text to process
     * @param modelType Type of AI model being used
     * @param maxTokens Maximum token limit
     * @return Processed text properly truncated to token limit
     */
    public String processForModel(String text, String modelType, int maxTokens) {
        // First apply basic sanitization
        String sanitized = sanitizerService.sanitizeAndOptimize(text, false, null);

        // Get the appropriate encoding for the model
        Encoding encoding = getEncodingForModel(modelType);

        // Tokenize the text
        IntArrayList tokens = encoding.encode(sanitized);

        // If within limits, return as is
        if (tokens.size() <= maxTokens) {
            return sanitized;
        }

        // Truncate tokens to the limit
        int[] truncatedTokens = Arrays.copyOf(tokens.toArray(), maxTokens);
        IntArrayList intArrayList = new IntArrayList();
        Arrays.stream(truncatedTokens).forEachOrdered(intArrayList::add);

        // Decode back to text
        return encoding.decode(tokens) + " [TRUNCATED]";
    }

    /**
     * Count tokens in a text for a specific model
     *
     * @param text      Text to count tokens for
     * @param modelType Type of AI model
     * @return Number of tokens
     */
    public int countTokens(String text, String modelType) {
        Encoding encoding = getEncodingForModel(modelType);
        return encoding.encode(text).size();
    }

    /**
     * Get the appropriate encoding based on model type
     *
     * @param modelType String representation of model type
     * @return Encoding appropriate for the model
     */
    private Encoding getEncodingForModel(String modelType) {
        // Map string model type to JTokkit ModelType or EncodingType
        switch (modelType.toLowerCase()) {
            case "gpt-4", "gpt-4-turbo":
                return registry.getEncodingForModel(ModelType.GPT_4);
            case "gpt-3.5-turbo":
                return registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
            case "claude-3", "claude-3-opus", "claude-3-sonnet":
                // Claude uses cl100k_base encoding like GPT-4
                return registry.getEncoding(EncodingType.CL100K_BASE);
            case "text-embedding-ada-002":
                return registry.getEncodingForModel(ModelType.TEXT_EMBEDDING_ADA_002);
            default:
                // Default to cl100k_base for unknown models
                return registry.getEncoding(EncodingType.CL100K_BASE);
        }
    }
}
