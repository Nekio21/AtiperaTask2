package umk.mat.jakuburb.AtiperaTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import umk.mat.jakuburb.AtiperaTask.git.model.dto.GitRepositoryDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port=0)
class AtiperaTaskApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${wiremock.server.port}")
	private static int wireMockPort;

	@DynamicPropertySource
	static void configureWiremock(DynamicPropertyRegistry registry){
		registry.add("https://api.github.com/", ()->"http://localhost:" + wireMockPort);
	}

	@BeforeEach
	void setupStub(){

		stubFor(get(urlEqualTo("/github/octocat"))
				.willReturn(okJson("""
						[
						  {
						    "repoName": "CSCI_477",
						    "owner": "octokat",
						    "branch": [
						      {
						        "name": "master",
						        "lastSha": "457e172fd3767dda14f557f6ea1a0626786043ca"
						      }
						    ]
						  },
						  {
						    "repoName": "HeliumAtom",
						    "owner": "octokat",
						    "branch": [
						      {
						        "name": "master",
						        "lastSha": "ba86edc07074be35a75e17c18e76a23682411084"
						      }
						    ]
						  },
						  {
						    "repoName": "Lotka-Volterra",
						    "owner": "octokat",
						    "branch": [
						      {
						        "name": "master",
						        "lastSha": "08bfc824ea0cbc888aed2385584531d2346ce7ad"
						      }
						    ]
						  },
						  {
						    "repoName": "MolecularFriction",
						    "owner": "octokat",
						    "branch": []
						  },
						  {
						    "repoName": "Pareto",
						    "owner": "octokat",
						    "branch": [
						      {
						        "name": "master",
						        "lastSha": "011f74e0196b5c0479ce40cd5d47cdd9d031a625"
						      }
						    ]
						  },
						  {
						    "repoName": "Runge-Kutta",
						    "owner": "octokat",
						    "branch": [
						      {
						        "name": "master",
						        "lastSha": "6ccda59f125f989c0e879df6f92c2c112b6c03da"
						      }
						    ]
						  },
						  {
						    "repoName": "TestRepo",
						    "owner": "octokat",
						    "branch": []
						  }
						]
						""")));

		stubFor(get(urlEqualTo("/github/notExistUser"))
				.willReturn(aResponse()
						.withStatus(404)  // Zwracamy kod 404
						.withBody("Not Found")));
	}

	@Test
	void shouldReturnRepositories() {
		ResponseEntity<GitRepositoryDto[]> response = restTemplate.getForEntity("/github/octocat", GitRepositoryDto[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(6, response.getBody().length);
		assertEquals("git-consortium", response.getBody()[0].repoName());
	}

	@Test
	void shouldReturn404(){
		ResponseEntity<String> response = restTemplate.getForEntity("/github/notExistUser", String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
