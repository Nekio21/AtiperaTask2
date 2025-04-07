# GitHub Repositories API

## Description
This application, built with Quarkus, fetches a list of public repositories for a given GitHub user, including the branches of each repository and their latest commit SHA. If the user does not exist, the API will return a 404 error with a relevant message.

## API Endpoints

### `GET /github/{user}`
Fetches all public repositories of the given GitHub user, including their branches with the latest commit SHA.

**Example Request:**
http://localhost:8080/github/octocat

```json
[
  {
    "repoName": "octocat",
    "owner": "hello-worId",
    "branchList": [
      {
        "name": "master",
        "lastSha": "7e068727fdb347b685b658d2981f8c85f7bf0585"
      }
    ]
  },
  {
    "repoName": "octocat",
    "owner": "git-consortium",
    "branchList": [
      {
        "name": "master",
        "lastSha": "b33a9c7c02ad93f621fa38f0e9fc9e867e12fa0e"
      }
    ]
  },
  {
    "repoName": "octocat",
    "owner": "octocat.github.io",
    "branchList": [
      {
        "name": "gh-pages",
        "lastSha": "c0e4a095428f36b81f0bd4239d353f71918cbef3"
      },
      {
        "name": "master",
        "lastSha": "3a9796cf19902af0f7e677391b340f1ae4128433"
      }
    ]
  },
  {
    "repoName": "octocat",
    "owner": "test-repo1",
    "branchList": [
      {
        "name": "gh-pages",
        "lastSha": "57523742631876181d95bc268e09fb3fd1a4d85e"
      }
    ]
  },
  {
    "repoName": "octocat",
    "owner": "Hello-World",
    "branchList": [
      {
        "name": "master",
        "lastSha": "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d"
      },
      {
        "name": "octocat-patch-1",
        "lastSha": "b1b3f9723831141a31a1a7252a213e216ea76e56"
      },
      {
        "name": "test",
        "lastSha": "b3cbd5bbd7e81436d2eee04537ea2b4c0cad4cdf"
      }
    ]
  },
  {
    "repoName": "octocat",
    "owner": "Spoon-Knife",
    "branchList": [
      {
        "name": "change-the-title",
        "lastSha": "f439fc5710cd87a4025247e8f75901cdadf5333d"
      },
      {
        "name": "main",
        "lastSha": "d0dd1f61b33d64e29d8bc1372a94ef6a2fee76a9"
      },
      {
        "name": "test-branch",
        "lastSha": "58060701b538587e8b4ab127253e6ed6fbdc53d1"
      }
    ]
  }
]
```

**Bad Request:**
http://localhost:8080/github/notExistUser

```json
{
  "status": 404,
  "message": "Not Found"
}
```
