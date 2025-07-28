package umk.mat.jakuburb.AtiperaTask;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import umk.mat.jakuburb.AtiperaTask.git.model.dto.GitRepositoryDto;

import java.util.Arrays;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8089)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AtiperaTaskApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@DynamicPropertySource
	static void init(DynamicPropertyRegistry registry) {
		registry.add("github.api.url", () -> "http://localhost:8089");
	}

	@BeforeEach
	void setupStubs() {
		stubUserRepos();

		stubRepoBranches("octocat", "CSCI_477", """
        [ { "name": "master", "commit": { "sha": "457e172fd3767dda14f557f6ea1a0626786043ca" } } ]
    """);

		stubRepoBranches("octocat", "HeliumAtom", """
        [ { "name": "master", "commit": { "sha": "ba86edc07074be35a75e17c18e76a23682411084" } } ]
    """);

		stubRepoBranches("octocat", "Lotka-Volterra", """
        [ { "name": "master", "commit": { "sha": "08bfc824ea0cbc888aed2385584531d2346ce7ad" } },
         { "name": "mini", "commit": { "sha": "012f74e0196b5c0479ce40cd5d47cdd9d031a625" } }
         ]
    """);

		stubRepoBranches("octocat", "MolecularFriction", "[]");

		stubRepoBranches("octocat", "Pareto", """
        [ { "name": "abc", "commit": { "sha": "011f74e0196b5c0479ce40cd5d47cdd9d031a625" } } ]
    """);

		stubRepoBranches("octocat", "Runge-Kutta", """
        [ { "name": "master", "commit": { "sha": "6ccda59f125f989c0e879df6f92c2c112b6c03da" } } ]
    """);
	}


	@Test
	void shouldReturnRepositories() {
		ResponseEntity<GitRepositoryDto[]> response = restTemplate.getForEntity("/github/octocat", GitRepositoryDto[].class);

		Optional<GitRepositoryDto> lotka = Arrays.stream(response.getBody())
				.filter(r -> r.repoName().equals("Lotka-Volterra"))
				.findFirst();

		Optional<GitRepositoryDto> molecular = Arrays.stream(response.getBody())
				.filter(r -> r.repoName().equals("MolecularFriction"))
				.findFirst();

		Optional<GitRepositoryDto> pareto = Arrays.stream(response.getBody())
				.filter(r -> r.repoName().equals("Pareto"))
				.findFirst();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(6, response.getBody().length);
		assertTrue(lotka.isPresent());
		assertTrue(molecular.isPresent());
		assertTrue(pareto.isPresent());
		assertEquals("CSCI_477", response.getBody()[0].repoName());
		assertEquals(0, Arrays.stream(response.getBody()).filter(repo->repo.repoName().equals("TestRepo")).count());
		assertEquals("octocat", response.getBody()[0].owner());
		assertEquals(0, molecular.get().branch().size());
		assertEquals(2,lotka.get().branch().size());
		assertEquals("011f74e0196b5c0479ce40cd5d47cdd9d031a625", pareto.get().branch().get(0).lastSha());
		assertEquals("abc", pareto.get().branch().get(0).name());
	}


	private void stubUserRepos() {
		stubFor(get(urlEqualTo("/users/" + "octocat" + "/repos"))
				.willReturn(okJson("""
            [
              { "name": "CSCI_477", "fork": false, "owner": { "login": "octocat" } },
              { "name": "HeliumAtom", "fork": false, "owner": { "login": "octocat" } },
              { "name": "Lotka-Volterra", "fork": false, "owner": { "login": "octocat" } },
              { "name": "MolecularFriction", "fork": false, "owner": { "login": "octocat" } },
              { "name": "Pareto", "fork": false, "owner": { "login": "octocat" } },
              { "name": "Runge-Kutta", "fork": false, "owner": { "login": "octocat" } },
              { "name": "TestRepo", "fork": true, "owner": { "login": "octocat" } }
            ]
            """)));
	}

	private void stubRepoBranches(String owner, String repo, String branchesJson) {
		stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo + "/branches"))
				.willReturn(okJson(branchesJson)));
	}
	@SpringBootApplication
	static class AppConfiguration {}
}
