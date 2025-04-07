package umk.mat.jakuburb.AtiperaTask.git.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.jakuburb.AtiperaTask.git.client.RepositoryClient;
import umk.mat.jakuburb.AtiperaTask.git.model.dto.GitRepositoryDto;
import umk.mat.jakuburb.AtiperaTask.git.model.github.BranchResponse;
import umk.mat.jakuburb.AtiperaTask.git.model.transform.BranchSummary;
import umk.mat.jakuburb.AtiperaTask.git.model.github.RepositoryResponse;

import java.util.List;
import java.util.stream.IntStream;

@RestController
public class GitController {

    @Autowired
    private RepositoryClient client;

    @GetMapping("/github/{username}")
    public List<GitRepositoryDto> getRepo(@PathVariable("username")String username){

        List<RepositoryResponse> repos = client.getRepos(username).stream().filter(repo->!repo.fork()).toList();

        List<List<BranchSummary>> branches = repos.stream()
                .map(repo -> {
                    List<BranchResponse> rawBranches = client.getBranches(repo.owner().login(), repo.name());
                    return rawBranches.stream()
                            .map(b -> new BranchSummary(b.name(), b.commit().sha()))
                            .toList();
                })
                .toList();

        List<GitRepositoryDto> gitRepositoryDto = IntStream.range(0, repos.size())
                .mapToObj(i -> new GitRepositoryDto(
                        repos.get(i).name(),
                        repos.get(i).owner().login(),
                        branches.get(i)))
                .toList();

        return gitRepositoryDto;
    }
}
