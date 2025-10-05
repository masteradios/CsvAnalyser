#!/bin/bash
# Update system packages
apt-get update -y
apt-get upgrade -y

# Install OpenJDK 17
apt-get install -y openjdk-17-jdk
update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-17-openjdk-amd64/bin/java 1
update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java

# Add Jenkins repo
wget -O /usr/share/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
cat <<EOF > /etc/apt/sources.list.d/jenkins.list
deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/
EOF

apt-get update -y
apt-get install -y jenkins
systemctl enable jenkins
systemctl start jenkins

# Install Docker
apt-get install -y ca-certificates curl gnupg lsb-release
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
cat <<EOF > /etc/apt/sources.list.d/docker.list
deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable
EOF

apt-get update -y
apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
systemctl enable docker
systemctl start docker



#Install Ansible
apt-get update -y
apt-get upgrade -y

# Install prerequisites
apt-get install -y software-properties-common gnupg

# Add the official Ansible PPA
add-apt-repository --yes --update ppa:ansible/ansible

# Refresh package lists
apt-get update -y

# Install Ansible
apt-get install -y ansible