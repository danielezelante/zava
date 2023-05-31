#!/usr/bin/env bash

set -x

readonly SITE='https://zetasys.net/gitea/api/v1'
readonly ORGN='zetasys.net'
readonly REPO='zava'
TOKEN=$(grep '^gitea\.key=' "$HOME/.gradle/gradle.properties" | sed -E 's/.*=//')

function fatal()
{
    echo "$1"
    exit 1
}

function uploadasset()
{
    echo "Uploading asset '$1' ..."
    local URL="$SITE/repos/$ORGN/$REPO/releases/$2/assets"
    curl -s --user "${USER}:${TOKEN}" -X 'POST' "$URL" \
        -H 'accept: application/json' \
        -H 'Content-Type: multipart/form-data' \
        -F "attachment=@$1" > /dev/null || fatal 'upload asset failed'
}
        
function makerelease()
{
    echo "Processing gitea release '$1' ..."
    local URL="$SITE/repos/$ORGN/$REPO/releases"
    local X
    X=$(curl -s -X 'DELETE' --user "${USER}:${TOKEN}" "$URL/tags/$1" -H 'accept: application/json' | jq '.errors')
    [[ -z "$X" ]] && echo "Deleted old release."
    echo "Creating new release ..."
    X=$(curl -s --json "{\"name\": \"$1\", \"prerelease\": true, \"tag_name\": \"$1\"}" --user "${USER}:${TOKEN}" "$URL" | jq '.id')
    
    [[ $X == 'null' ]] && fatal 'error creating release'
    
    uploadasset "build/distributions/zava-shadow-$VER.zip" "$X"
}


[[ -z "$1" ]] && fatal 'version required'
VER="$1"
makerelease "$VER" 

#.
