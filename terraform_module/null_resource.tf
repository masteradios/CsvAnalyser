resource "null_resource" "copy_inventory" {
  depends_on = [aws_instance.public, aws_eip.public_ip]

  provisioner "file" {
    source      = "../ansible/inventories/ansible_inventory.ini"
    destination = "/home/ubuntu/ansible_inventory.ini"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("C:/Users/reach/Downloads/dc.pem")
      host        = aws_eip.public_ip.public_ip
    }
  }
}
