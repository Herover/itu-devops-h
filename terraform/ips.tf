resource "digitalocean_floating_ip" "minitwit-external" {
  droplet_id = digitalocean_droplet.web[0].id
  region     = digitalocean_droplet.web[0].region
}
