#!/usr/bin/env bash
[[ -z "$1" ]] && exit 1
git push --delete origin $*
git tag --delete $*
#.
