#!/bin/bash
# Update system packages
apt-get update -y
apt-get upgrade -y

# Install OpenJDK 17
apt-get install -y openjdk-17-jdk
update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-17-openjdk-amd64/bin/java 1
update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java




#sqa_60a407fe8b2dec5fe4c51273b77c5efc69b3ba3c