package io.vels.readme.generator.service;

import io.vels.readme.generator.dtos.Commit;
import io.vels.readme.generator.dtos.DiffEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ReadmeGeneratorService {


    private final AiClientService aiClientService;
    private final GitHubService gitHubService;

    @Value("classpath:/prompts/analyze-generate-commit-difference.st")
    private String genCommitDiffMsgPromptTemplateUri;


    public ReadmeGeneratorService(AiClientService aiClientService, GitHubService gitHubService) {
        this.aiClientService = aiClientService;
        this.gitHubService = gitHubService;
    }

    public String generateFromCommit(String owner, String repo, String commitId) {

        // Get the commit content from gitHub
        Commit commit = gitHubService.getCommitContents(owner, repo, commitId);

        // Parse the content to extract the Patches
        String patches = commit.files().stream().map(DiffEntry::patch).collect(Collectors.joining("\n"));

        // Invoke the AI
        return aiClientService.generatePrompt(genCommitDiffMsgPromptTemplateUri, patches);
    }
}
