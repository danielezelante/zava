#!/usr/bin/env bash

RP=$(realpath "$0")
MYDIR=$(dirname "$RP")

syntaxpar()
{
	echo "syntax: deploy.sh USERNAME HOST RUC_TAG"
	exit 2
}

fatal()
{
	echo "$1"
	exit 1
}

readonly USERNAME="$1"
[[ -z $USERNAME ]] && syntaxpar

readonly HOST="$2"
[[ -z $HOST ]] && syntaxpar

readonly RUC_TAG="$3"
[[ -z $RUC_TAG ]] && syntaxpar

[[ -n $(git status --porcelain) ]] && fatal "you should commit before"

MYVER=$("$MYDIR/../scmrev" | sed -r 's/-g.*//')
[[ -z "$MYVER" ]] && fatal "cannot get local version"
echo "$MYVER" | grep -q '+g' && fatal "local version is not a release"

readonly HA="/home/$RUC_TAG"
readonly HAUP="$HA/dbupgrade"
readonly SSHURL="$USERNAME@$HOST:$HA/"

readonly OUT='build/libs'
x='negotium'
scp "$x/$OUT/$x-$MYVER-all.jar" "${SSHURL}$x.jar" || fatal "scp $x failed"
x='totem'
scp "$x/$OUT/$x-$MYVER-all.jar" "${SSHURL}$x.jar" || fatal "scp $x failed"
x='dbupgrade'
scp "$x/$OUT/$x-$MYVER-all.jar" "${SSHURL}dbupgrade/$x.jar" || fatal "scp $x failed"

ssh "$USERNAME@$HOST" "java -jar $HAUP/dbupgrade.jar $HAUP/dbupgrade.properties" || fatal "deploy failed"

./tools/news-sm.sh "$MYVER $RUC_TAG" "Release $MYVER deployed to customer $RUC_TAG." || fatal "news failed"

#.
