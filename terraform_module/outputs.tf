output "public_ip" {
  value = aws_instance.public.public_ip
}

output "private_ips" {
  value = [for i in aws_instance.private : i.private_ip]
}

output "vpc_id" {
  value = aws_vpc.main.id
}

output "elastic_public_ip" {
  value = aws_eip.public_ip.public_ip
}

