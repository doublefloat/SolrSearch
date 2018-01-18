#!/bin/bash

LOG_FILE=/tmp/pyresttest.log

execute(){
	pyresttest $1 $2 &>>$LOG_FILE
	if (( $? == 0)); then
		echo "[INFO]Test(s) is passed in '$2'"
	else
		echo "[ERROR]Test(s) cant't pass in '$2'"
		exit 1
	fi
}

scan_dir(){
	if [[ -f $2 ]]; then
		execute $1 $2
	fi  

	for i in $2/* ; do
		if [[ -d $i ]]; then
			scan_dir $1 $i
		elif [[ -f $i ]]; then
			execute $1 $i
			if (( $? !=0 )); then
				exit 1
			fi
		fi  
	done
}

>$LOG_FILE
scan_dir $1 $2
