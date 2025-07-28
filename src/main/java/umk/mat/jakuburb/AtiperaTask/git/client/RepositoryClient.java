package umk.mat.jakuburb.AtiperaTask.git.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import umk.mat.jakuburb.AtiperaTask.git.model.github.BranchResponse;
import umk.mat.jakuburb.AtiperaTask.git.model.github.RepositoryResponse;

import java.util.List;

@FeignClient(name="ReposClient", url="${github.api.url}")
public interface RepositoryClient {

    @GetMapping("/users/{username}/repos")
    List<RepositoryResponse> getRepos(@PathVariable("username") String username);

    @GetMapping("/repos/{owner}/{repo}/branches")
    List<BranchResponse> getBranches(@PathVariable("owner") String owner, @PathVariable("repo") String repositoryName);
}
