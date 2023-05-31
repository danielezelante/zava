#!/usr/bin/env bash

function fatal()
{
	echo "$1"
	exit 1
}

readonly domain="zetasys.net"
readonly project='sigeco'

readonly title="$1"
readonly text="$2"

[[ -z "$1" ]] && fatal "title required"
[[ -z "$2" ]] && fatal "text required"

sendEmail -q -f "$USER@$domain" -u "$title" -m "$text" -s "$domain" -t "$project@$domain"

#.
