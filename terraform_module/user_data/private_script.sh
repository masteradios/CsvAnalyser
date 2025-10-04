#!/bin/bash
# Update system packages
apt-get update -y
apt-get upgrade -y

# Install OpenJDK 17
apt-get install -y openjdk-17-jdk
update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-17-openjdk-amd64/bin/java 1
update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java




#squ_cfd0e8d34c3e5ce05580238d6146fbd3e44ad223 