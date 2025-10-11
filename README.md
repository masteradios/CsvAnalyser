# CsvAnalyser – CI/CD Pipeline with Jenkins, SonarQube & Ansible

##  Overview
**CsvAnalyser** is a Spring Batch–based backend application designed to automate CSV log analysis.  
It is integrated with a **CI/CD pipeline** powered by **Jenkins**, **SonarQube**, and **Ansible**, ensuring smooth build, test, and deployment workflows.

---

##  Tech Stack
- **Spring Boot + Spring Batch** — Backend processing for CSV logs  
- **Maven** — Build and dependency management  
- **Jenkins** — Continuous Integration and Deployment  
- **SonarQube** — Static code analysis and quality gates  
- **Ansible** — Automated remote deployment  
- **Email Extension Plugin** — Build notifications  

---

## Terraform Infrastructure

The `terraform_module` folder contains Terraform scripts to provision cloud infrastructure required for deploying **CsvAnalyser**.  

### Resources Created

| Resource Type | Description |
|---------------|-------------|
| **VPC / Network** | A dedicated virtual network to host application servers securely. |
| **Subnets** | Public and private subnets for organizing resources and controlling access. |
| **Security Groups / Firewall Rules** | Security groups defining allowed inbound and outbound traffic (e.g., SSH, HTTP). |
| **EC2 / Virtual Machines** | 1 Public Instance (t2.large) which runs Jenkins and Sonarqube and 2 Private Instances (t2.medium) where the CsvAnalyser JAR is deployed via Ansible|
| **Outputs** | Public IP, Private Ip and Elastic IP Assigned to the public instance|

## Ansible

Ansible automates the **deployment and management** of the CsvAnalyser application on remote servers. Here's a breakdown of its tasks:

1. **Check for JAR Artifact**
   - Validates if the new `CsvAnalyser-0.0.1-SNAPSHOT.jar` exists in the `artifacts/` folder on the Jenkins host.
   - Prevents deployment if the artifact is missing.

2. **Backup Existing JAR (Optional)**
   - Can backup the current running JAR on the remote host before replacing it.
   - Helps in rollback in case of deployment failure.

3. **Copy New JAR to Remote Hosts**
   - Transfers the newly built JAR from the Jenkins control node to `/opt/CsvAnalyser/` on the target servers.
   - Sets appropriate permissions (`0755`) for execution.


##  Repository Structure
CsvAnalyser/  
│  
├── backend/ # Spring Batch application (CSV Analyzer)  
│   ├── src/main/java/... # Source code for the application  
│   ├── pom.xml # Maven build file  
│   └── target/ # Generated JAR file after build  
│  
├── ansible/ # Ansible role & playbook for deployment  
│   ├── playbook/  
│   │   └── main.yml # Main Ansible playbook entry point  
│   └── roles/  
│       └── deploy_jar/ # Role handling deployment, backup, restart  
│  
├── terraform_module/ # Terraform configuration for provisioning infra  
│   ├── main.tf  
│   ├── variables.tf  
│   └── outputs.tf  
│  
├── Jenkinsfile # Jenkins pipeline automation  
└── README.md  

---

## SonarQube Setup

### Integration Steps

1. Install and configure SonarQube Server.
2. In Jenkins → **Manage Jenkins** → **Tools** → **SonarQube Scanner**, add a scanner named `sonarqubeCommunity`.
3. Under **Manage Jenkins** → **Configure System** → **SonarQube servers**, add:  
   - **Name**: `sonarqube-community`  
   - **Server URL**  
   - **Authentication Token** (credentials)


## Email Notification Setup

To enable build result emails:

1. Install **Email Extension Plugin** in Jenkins.  
2. Configure under:  
   **Manage Jenkins** → **Configure System** → **Extended E-mail Notification**

Add the following configuration:

- **SMTP Server**: smtp.gmail.com  
- **Port**: 587  
- **Enable authentication**  
- **Test sending email**  

---

## Running the Pipeline

Once your `Jenkinsfile` is committed:

1. Create a new **Pipeline Job** in Jenkins.  
2. Set **SCM** to your GitHub repository.  
3. Run the pipeline — it will:

Clone the repo  
Build the Spring Boot JAR  
Run SonarQube analysis  
Copy JAR into `artifacts/`  
Deploy via Ansible  
Send email notifications  
