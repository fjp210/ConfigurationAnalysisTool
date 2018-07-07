#!/bin/bash

bin=$(dirname "$0")
bin=$(cd "$bin";pwd)
workspace=${bin}/../ 
cd $workspace 

#if [ ! -e logs/worker.pid ]
#then
#    lastpid=""
#else
#    lastpid=$( cat logs/worker.pid  )
#fi
#
#if [ "$lastpid" != "" ] && ps $lastpid >/dev/null
#then
#    echo "worker was already running as pid $lastpid, please stop it first ."
#    exit 1
#fi

if [ -e "../config/worker-env.sh" ]
then
 source ../config/worker-env.sh
fi 

if [ ! -e "./data" ]
then
    mkdir data
fi

if [ "$JAVA_HOME" == ""  ]
then
    echo "NO JAVA_HOME set, use $(which java) "
    java=java
else
    java=$JAVA_HOME/bin/java
fi 

classpath=""
for jar in $(ls lib/*.jar)
do
   classpath=$classpath:$jar
done 
java_opts="-cp "$classpath
if [ "$1" == "agent" ]
then
	#ps -ef |grep AgentMain |grep -v grep |awk '{print $2}'|xargs kill -9  2>&1
	$java $java_opts edu.buaa.rse.dotx.agent.AgentMain  2>&1   &
	pid=$!
	echo $pid > log/agent.pid
fi
if [ "$1" == "worker" ]
then
	#ps -ef |grep WorkerMain |grep -v grep |awk '{print $2}'|xargs kill -9  2>&1
	$java $java_opts edu.buaa.rse.dotx.worker.WorkerMain  2>&1   &
	pid=$!
	echo $pid > log/worker.pid
fi

