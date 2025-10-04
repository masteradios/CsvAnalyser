[public]
${public_ip}

[private]
%{ for ip in private_ips ~}
${ip}
%{ endfor ~}
