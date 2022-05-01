resource "digitalocean_loadbalancer" "lb" {
  name                     = "lb-1"
  region                   = "ams3"
  enable_backend_keepalive = true
  droplet_tag              = "web-lb"

  forwarding_rule {
    entry_port      = 80
    entry_protocol  = "http"

    target_port     = 80
    target_protocol = "http"
  }

  sticky_sessions {
    type               = "cookies"
    cookie_name        = "DO-LB"
    cookie_ttl_seconds = 300
  }

  healthcheck {
    protocol                 = "http"
    port                     = 80
    path                     = "/"
    check_interval_seconds   = 10
    response_timeout_seconds = 5
    unhealthy_threshold      = 3
    healthy_threshold        = 5
  }
}
