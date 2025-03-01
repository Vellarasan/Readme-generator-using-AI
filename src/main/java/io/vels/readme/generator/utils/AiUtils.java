package io.vels.readme.generator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ResourceUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class AiUtils {

    Logger log = LoggerFactory.getLogger(AiUtils.class);

    private final ChatClient chatClient;

    public AiUtils(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String invokeWithPromptAndValues(String prompt) {

        ChatResponse chatResponse = chatClient
                .prompt(prompt)
                .call()
                .chatResponse();
        return chatResponse != null ?
                chatResponse.getResult().getOutput().getText() :
                "No Response received. Please try again!";
    }

    public String constructPrompt(String promptUri, String... values) {
        String promptAsString = ResourceUtils.getText(promptUri);

        // This will dynamically assign the values to the prompt
        return StringUtils.formatWithArray(promptAsString, values);

    }
}
