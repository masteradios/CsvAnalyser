variable "region" {
  default = "us-east-1"
}

variable "vpc_cidr" {
  default = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  default = "10.0.1.0/24"
}

variable "private_subnet_cidr" {
  default = "10.0.2.0/24"
}

variable "public_instance_type" {
  default = "t2.micro"
}

variable "private_instance_type" {
  default = "t2.micro"
}

variable "key_name" {
  description = "Name of the existing SSH key pair"
}

variable "ami_id" {
  description = "AMI ID for the EC2 instances"
}
