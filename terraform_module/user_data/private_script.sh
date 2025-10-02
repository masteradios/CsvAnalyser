#!/bin/bash
# Update system packages
sudo apt update -y
sudo apt upgrade -y

# Install OpenJDK 17
sudo apt install -y openjdk-17-jdk
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-17-openjdk-amd64/bin/java 1
sudo update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java