## This script tags the commit and increaments the  
#!/bin/bash
set -e

SCRIPT=$0
ARG=$1
REPO_TYPE=$2

TAG_REST_URL="https://{GIT_URL}/rest/api/1.0/projects/{PROJECT_KEY}/repos/{REPO_NAME}/tags"

if [ "${REPO_TYPE}" = "nodejs" ];
then
  BASE_VERSION=$(cat package.json | grep -i "version" | cut -d: -f2 | sed s/[\",]//g | tr -d \'[:space:] | sed "s/\.[0-9]*$//")
elif [ "${REPO_TYPE}" = "gradle" ];
then
  BASE_VERSION=$(cat build.gradle | grep -i "^version" | cut -d= -f2 | tr -d \'[:space:] | cut -d- -f1 | sed "s/\.[0-9]*$//")
fi

validation_error() {
  echo "Not inside a git repo. Exiting."
  exit 1
}

usage() {
  echo -n " ${SCRIPT} [OPTIONS] <nodejs|gradle>

  Versions application using git tags

  Options:

  -b|--bump     Increment the app version
  -v|--version  Fetch current version
  "
  exit 1
}

bump() {
  echo "Current base version is ${BASE_VERSION} for ${SERVICE}"
  CURRENT_PATCH_VERSION=$(curl -u ${GIT_AUTH_USR}:${GIT_AUTH_PSW} -X GET -H "Content-Type: application/json" ${TAG_REST_URL} | jq '.values[].id' | sed "s/refs\/tags\///g" | sed "s/\"//g" | grep "^${BASE_VERSION}" | head -1 | cut -d. -f3)

  NEXT_PATCH_VERSION=$((CURRENT_PATCH_VERSION+1))

  echo "patch number is ${NEXT_PATCH_VERSION}"
  NEXT_VERSION="${BASE_VERSION}.${NEXT_PATCH_VERSION}"

  echo "Next app version would be ${NEXT_VERSION}"
  curl -u ${GIT_AUTH_USR}:${GIT_AUTH_PSW} -X POST -H "Content-Type: application/json" ${TAG_REST_URL} \
   -d "{
   \"name\": \"$NEXT_VERSION\",
   \"startPoint\": \"${GIT_COMMIT}\",
   \"message\": \"Bumping up tag\"
   }"
}

getVersion() {
  CURRENT_PATCH_VERSION=$(curl -u ${GIT_AUTH_USR}:${GIT_AUTH_PSW} -X GET -H "Content-Type: application/json" ${TAG_REST_URL} | jq '.values[].id' | sed "s/refs\/tags\///g" | sed "s/\"//g" | grep "^${BASE_VERSION}" | cut -d. -f3 | sort -g | tail -n 1)
  echo $BASE_VERSION.$CURRENT_PATCH_VERSION
}

[[ -d .git ]] || validation_error

case $ARG in
  -b|--bump)
    bump
    ;;
  -v|--version)
    getVersion
    ;;
  *)
    usage
    ;;
esac
