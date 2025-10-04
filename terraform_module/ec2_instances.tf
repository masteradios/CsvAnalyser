resource "aws_instance" "public" {
  ami             = var.ami_id
  instance_type   = var.public_instance_type
  subnet_id       = aws_subnet.public.id
  key_name        = var.key_name
  vpc_security_group_ids      = [aws_security_group.public_sg.id] 
  user_data = file("user_data/public_script.sh")

  tags = {
    Name = "public-jenkins"
  }

}

resource "aws_instance" "private" {
  count           = 2
  ami             = var.ami_id
  instance_type   = var.private_instance_type
  subnet_id       = aws_subnet.private.id
  key_name        = var.key_name
  vpc_security_group_ids      = [aws_security_group.private_sg.id] 
  user_data = file("user_data/private_script.sh")

  tags = {
    Name = "private-app-${count.index + 1}"
  }
}
