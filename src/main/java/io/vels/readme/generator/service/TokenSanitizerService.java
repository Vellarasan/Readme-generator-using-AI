package io.vels.readme.generator.service;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for sanitizing and optimizing text before tokenizing for AI model input.
 */
@Service
public class TokenSanitizerService {

    private static final Logger logger = LoggerFactory.getLogger(TokenSanitizerService.class);

    // Common stop words that might be removed for optimization if required
    private static final Set<String> STOP_WORDS = ImmutableSet.of(
            "a", "an", "the", "and", "or", "but", "is", "are", "was", "were",
            "in", "on", "at", "to", "for", "with", "by", "about", "like", "of"
    );

    // Patterns for detecting common issues
    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\p{P}\\p{Z}]");
    private static final Pattern CONSECUTIVE_SPACES = Pattern.compile("\\s{2,}");
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");

    /**
     * Main method to sanitize and optimize text for AI model processing
     *
     * @param inputText       Raw text to process
     * @param removeStopWords Whether to remove common stop words (for some use cases)
     * @param maxTokens       Optional maximum number of tokens to keep, approximation only
     * @return Sanitized and optimized text
     */
    public String sanitizeAndOptimize(String inputText, boolean removeStopWords, Integer maxTokens) {
        StopWatch watch = new StopWatch();
        watch.start("sanitization");

        if (StringUtils.isBlank(inputText)) {
            return "";
        }

        // STEP 1: Basic sanitization
        String sanitized = sanitizeText(inputText);

        // STEP 2: Optional stop word removal (careful, may change meaning)
        if (removeStopWords) {
            sanitized = removeStopWords(sanitized);
        }

        // STEP 3: Length optimization if maxTokens is provided
        if (maxTokens != null && maxTokens > 0) {
            sanitized = approximateTokenLimit(sanitized, maxTokens);
        }

        watch.stop();
        logger.debug("Text sanitization completed in {} ms", watch.getTotalTimeMillis());

        return sanitized;
    }

    /**
     * Core sanitization method for cleaning text
     */
    private String sanitizeText(String text) {
        if (text == null) {
            return "";
        }

        // Remove HTML tags
        String result = HTML_TAG_PATTERN.matcher(text).replaceAll(" ");

        // Sanitize special characters
        result = StringEscapeUtils.unescapeHtml4(result); // Convert HTML entities like &amp; to &

        // Mask/remove potentially sensitive information
        result = maskSensitiveInfo(result);

        // Remove non-printable characters
        result = SPECIAL_CHARS_PATTERN.matcher(result).replaceAll("");

        // Normalize whitespace 
        result = CONSECUTIVE_SPACES.matcher(result).replaceAll(" ");

        // Trim leading/trailing whitespace
        return result.trim();
    }

    /**
     * Masks sensitive information like emails and URLs if needed
     */
    private String maskSensitiveInfo(String text) {
        // Replace emails with a placeholder
        Matcher emailMatcher = EMAIL_PATTERN.matcher(text);
        String maskedText = emailMatcher.replaceAll("[EMAIL]");

        // Optionally replace URLs - comment out if you want to keep URLs
        Matcher urlMatcher = URL_PATTERN.matcher(maskedText);
        return urlMatcher.replaceAll("[URL]");

        // Add more patterns for other sensitive data like phone numbers, SSNs, etc.
    }

    /**
     * Removes common stop words to reduce token count
     * Note: Only use when appropriate for your use case
     */
    private String removeStopWords(String text) {
        List<String> words = new ArrayList<>(List.of(text.split("\\s+")));
        words = words.stream()
                .filter(word -> !STOP_WORDS.contains(word.toLowerCase()))
                .toList();
        return String.join(" ", words);
    }

    /**
     * Basic approximation of token limits
     * This is a rough estimate - actual tokenization varies by model
     */
    private String approximateTokenLimit(String text, int maxTokens) {
        // Very rough approximation: ~4 chars per token for English
        // For production use, consider using the actual tokenizer of your AI model
        int estimatedTokens = (int) Math.ceil(text.length() / 4.0);

        if (estimatedTokens <= maxTokens) {
            return text;
        }

        // Simple truncation - in production consider more sophisticated approaches
        int approximateLength = maxTokens * 4;
        if (approximateLength < text.length()) {
            // Try to cut at a word boundary
            int lastSpace = text.lastIndexOf(' ', approximateLength);
            if (lastSpace > 0) {
                return text.substring(0, lastSpace) + " [TRUNCATED]";
            } else {
                return text.substring(0, approximateLength) + " [TRUNCATED]";
            }
        }

        return text;
    }
}
