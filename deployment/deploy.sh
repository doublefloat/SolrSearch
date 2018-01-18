#!/bin/bash

if [[ ! -n "$1" ]]; then
	echo "[ERROR]Please enter a command"
	exit 1
fi

case $1 in
	init)
		if [[ -n "$2" ]]; then
			./automize_mvn.sh $2 $3


		else
			echo "[ERROR]Please enter the directory which the pom.xml is in"
			exit 1
		fi
		;;
	distribute)
				ansible-playbook ansible/env_init.yml --ask-pass
		;;
	start)
		ansible-playbook -u=root ansible/start_up.yml --ask-pass
		;;
	restart)
		ansible-playbook -u=root ansible/restart.yml --ask-pass
		;;
	stop)
		ansible-playbook -u=root ansible/shut_down.yml --ask-pass
		;;
esac
