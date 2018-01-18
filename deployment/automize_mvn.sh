#!/bin/bash

if [[ -n "$1" ]]; then
  if [[ -e "$1/pom.xml" ]]; then
		echo "[INFO]Enter directory: '$1'"
    cd $1
  else
		echo "[ERROR]Cant't find pom on directory: '$1'"
		exit 1
	fi
else
	echo "[ERROR]Please enter the directory in which pom.xml is"
	exit 1
fi

SKIP_TEST=false
if [[ -n "$2" ]]; then
  if [[ "$2" == "skip-test" ]]; then
    SKIP_TEST=true
    echo "[INFO]Test is skipped"
  fi
fi

if [[ "$SKIP_TEST" == "false" ]]; then
	echo "[INFO]Executing tests..."
  { mvn test; } >> /tmp/mvn.log
  if (( $? == 0 )); then
    echo "[INFO]Test is successful"
  else
    echo "[ERROR]Test is failed"
    exit 1
  fi
fi

echo "[INFO]Packaging..."
{ mvn package -Dmaven.test.skip=true; } >> /tmp/mvn.log

if (( $? == 0 )); then
	echo "[INFO]Package is successful"
else
	echo "[ERROR]Package is failed"
	exit 1
fi

echo "[INFO]Copy 'target/*.jar' to 'deployment/app/' ..."
cp target/*.jar deployment/app/

if (( $? == 0 )); then
	echo "[INFO]Target jar is copied"
else
	echo "[ERROR]Copy is failed"
	exit 1
fi


