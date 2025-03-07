package io.vels.readme.generator.controller;

import io.vels.readme.generator.service.ReadmeGeneratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/generate/readme")
public class ReadmeGeneratorController {

    private final ReadmeGeneratorService readmeGeneratorService;

    public ReadmeGeneratorController(ReadmeGeneratorService readmeGeneratorService) {
        this.readmeGeneratorService = readmeGeneratorService;
    }

    @GetMapping("/commit/{owner}/{repo}/{commitId}")
    String generateFromCommit(@PathVariable(value = "owner") String owner,
                              @PathVariable(value = "repo") String repo,
                              @PathVariable(value = "commitId") String commitId) {
        return readmeGeneratorService.generateFromCommit(owner, repo, commitId);
    }

    @GetMapping("/commit/{owner}/{repo}/commits")
    String generateFromAllCommits(@PathVariable(value = "owner") String owner,
                                  @PathVariable(value = "repo") String repo) {
        return readmeGeneratorService.generateFromAllCommits(owner, repo);
    }

}
