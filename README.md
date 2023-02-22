# zava
useful(less) java classes by zetasys.net

self-hosted development sequence:
- `./wipver` (optional to get actual wip version)
- modify
- `./gradlew build` (optional to pre-check) if fail go back to *modify*
- `./wipver X.Y.Z` (optional semantic version change depending on what was modified)
- `./release`
- create release on github web for the existing tag: *vX.Y.Z*
