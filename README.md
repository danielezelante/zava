# zava
useful(less) java classes by zetasys.net

self-hosted development sequence:
- `./newver X.Y.Z`
- modify
- `git commit`
- `git push` optional
- `./gradlew build`
- test, if fail go back to *modify*
- `git tag vX.Y.Z` (with the same *X.Y.Z*)
- `git push`
- `git push --tags`
- create release on githb web for the tag: *vX.Y.Z*
