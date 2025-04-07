package umk.mat.jakuburb.AtiperaTask.git.model.dto;

import umk.mat.jakuburb.AtiperaTask.git.model.transform.BranchSummary;

import java.util.List;

public record GitRepositoryDto(String repoName, String owner, List<BranchSummary> branch) {
}
