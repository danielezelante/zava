#!/usr/bin/env bash

readonly SITE='https://gitea.zetasys.net/api/v1'
readonly ORGN='zetasys.net'
readonly REPO='zava'

function fatal()
{
    echo "$1"
    exit 1
}

        
function makerelease()
{
    echo "Processing github '$1' ..."
    gh release create "$1" --generate-notes -p
}


[[ -z "$1" ]] && fatal 'version required'
VER="$1"
makerelease "$VER" 
exit 0
#.
