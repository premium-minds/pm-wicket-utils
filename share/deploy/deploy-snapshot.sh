#!/bin/bash

set -e

setup_git() {
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis CI"
}

if [ "$TRAVIS_BRANCH" == "master" ] || [ "$TRAVIS_BRANCH" == "wicket-6.x" ] ; then 
  mvn -DskipTests=true -B --settings share/deploy/travis-settings.xml deploy
  setup_git
  mvn -B javadoc:javadoc scm-publish:publish-scm > /dev/null
  echo "deployed to snapshot release"
else
  echo "this is not master or wicket-6.x, didn't deploy to snapshot"
fi


