#!/bin/bash

# Installs openjdk 17 and maven 3.8.4
# Maven is only available after running this script, not from new terminals
# Must run as root.

echo "Updating..."
apt-get update

echo "Installing openjdk-17-jdk and openjdk-17-jre"
until apt-get install -y "openjdk-17-jre:amd64=17.*"; do sleep 1; done
until apt-get install -y "openjdk-17-jdk:amd64=17.*"; do sleep 1; done

echo "Installing "
# Note: default download server (dlcdn.apache.org) returns 503 to Digital Ocean IPs at the moment
# 2022-03-03
wget -O "/tmp/maven.tar.gz" https://downloads.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz
tar zxvf /tmp/maven.tar.gz -C /opt
export PATH=$PATH:/opt/apache-maven-3.8.4/bin