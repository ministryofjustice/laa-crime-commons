## What

[Link to story](https://dsdmoj.atlassian.net/browse/LCAM-XXX)

Describe what you did and why. Feel free to include other information that will help a reviewer. e.g. related stories, screenshots, logs etc.

## Checklists

In general these checks apply to most PRs. There will be exceptions, in these cases make it clear what is different and why.
e.g. DRAFT PR because you want early feedback, change only warrants a more targeted run of functional tests, etc.

Some checks can be completed after asking for review, but should be done before merging. Consider what checks will make a reviewers job easier.

### Git housekeeping
- [ ] The pull request is not labelled as `WIP/DRAFT`.
- [ ] The pull request has a title format of `{JIRA ID} - Description`.
- [ ] A link to the Jira ticket is provided along with a description giving context and rationale for the changes, in the `What` section of the PR.
- [ ] GitHub is not reporting any conflicts with the `main` branch.
- [ ] Nothing unexpected is included in the changes, when compared to the `main` branch.
- [ ] All of the pull request triggered checks (such as Build and Test, CodeQL and Snyk) have passed.
- [ ] The commit messages have meaningful descriptions.
- [ ] If changes include UI modifications, include before and after screenshots to aid reviewers.

### Change housekeeping
- [ ] Relevant areas in the documentation have been updated to reflect the changes. This may include runbooks, alerts, confluence, etc.
- [ ] Attach link / screenshot from successful CI run of UI `@smoke` tests for this branch using [ExecuteUiTests workflow][1].
- [ ] Attach link / screenshot from successful CI run of **ALL** API tests for this branch using [ExecuteApiTests workflow][2].

[1]: https://github.com/ministryofjustice/laa-crimeapps-maat-functional-tests/actions/workflows/ExecuteUiTests.yaml
[2]: https://github.com/ministryofjustice/laa-crimeapps-maat-functional-tests/actions/workflows/ExecuteApiTests.yaml
