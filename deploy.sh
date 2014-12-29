#!/bin/bash

set -e

mvn clean deploy
mvn javadoc:javadoc scm-publish:publish-scm

