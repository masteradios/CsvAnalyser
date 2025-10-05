resource "local_file" "ansible_inventory" {
  content = templatefile("${path.module}/inventory.tpl", {
    public_ip   = aws_eip.public_ip.public_ip
    private_ips = [for i in aws_instance.private : i.private_ip]
  })
  filename = "../ansible/inventories/ansible_inventory.ini"
}
