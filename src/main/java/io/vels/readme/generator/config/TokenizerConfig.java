package io.vels.readme.generator.config;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.EncodingRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenizerConfig {

    @Bean
    public EncodingRegistry encodingRegistry() {
        return Encodings.newDefaultEncodingRegistry();
    }

}
