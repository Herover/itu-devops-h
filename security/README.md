# Minitwit security assessment

This brief report will describe our perceived security situation.

Context: our web application is a Java based program, our servers run linux and all services run in docker containers.

## Identification

### Assets

These are public facing servers and services.

web-1 (164.92.150.233)
| Port | Service                         |
| ---- | ------------------------------- |
| 22   | ssh                             |
| 80   | Minitwit web application (http) |
| 9100 | Node exporter metrics (http)    |


web-2 (206.189.13.233)
| Port | Service                         |
| ---- | ------------------------------- |
| 22   | ssh                             |
| 80   | Minitwit web application (http) |
| 9100 | Node exporter metrics (http)    |


db-1 (159.223.236.108)
| Port | Service                      |
| ---- | ---------------------------- |
| 22   | ssh                          |
| 5432 | postgresql                   |
| 9100 | Node exporter metrics (http) |


monitoring-1(167.71.78.68)
| Port | Service                      |
| ---- | ---------------------------- |
| 22   | ssh                          |
| 3000 | Grafana (http)               |
| 3100 | Loki (http)                  |
| 9090 | Prometheus (http)            |
| 9100 | Node exporter metrics (http) |


drone-1(159.223.9.94)
| Port | Service                              |
| ---- | ------------------------------------ |
| 22   | ssh                                  |
| 80   | Drone UI (http)                      |
| 3000 | Drone-docker-runner dashboard (http) |
| 9090 | Prometheus (http)                    |
| 9100 | Node exporter metrics (http)         |



### Threats

We consider the following to be technical threats against our web application

* Information leakage through SQL injection
* XSS/script injection
* Unauthorized access to user data through faulty authentication mechanism
* Security vulnerabilities in dependencies
* Supply chain attacks
* We also consider security vulnerabilities in default operating system programs and services such as Drone as possible threats. Supply chain attacks (for example malicious docker images in trusted repositories) are also considered threats to our systems.

### Scenarios

1. A high severity vulnerability is found in a third party java dependency, widespread exploitation leads to information leaks or remote code execution.
2. A faulty authentication mechanism allows unauthorized users to access other users, leading to information leaks and impersonation.
3. A XSS vulnerability leads to user impersonation, information leaks.
4. A vulnerability is found in Drones access management, leading to remote code execution, source code leaks, secrets leaks.
5. A malicious actor gets push access to a code or docker dependency, leading to remote code execution and possible information and secrets leaks.


## Analysis

We will use the following matrix to review our scenarios

|          | Low impact | Medium impact | High impact |
| -------- | ---------- | ------------- | ----------- |
| Likely   | 🟩         | 🟥            | 🟥          |
| Possible | 🟩         | 🟧            | 🟥          |
| Unlikely | 🟩         | 🟩            | 🟧          |

(Numbers refor to scenarios listed above.)

1. 🟧 As we have only used dependencies with a commercial development team, we consider the risk of it happening to be less likely than for hobby projects, but not unlikely (possible). The impact can range widely, so we put it as medium.
2. 🟩 We used a battle tested encryption library to we consider this unlikely. Business impact would be tangible (medium).
3. 🟧 XSS vulnerabilities appear common, so we consider it possible. Same business impact as 2.
4. 🟥 We consider it possible to see a vulnerability in drone as it is a valuable target with a open source model. High impact.
5. 🟧 Risk the same as 1.

To mitigate the risks from these scenarios, we will deploy code scanning using dependency scans and container scans. We will also perform audits of our web application.

## Pen test

A pentest was performed using automated tools and manual techniques. 

Tools used were skipfish and WMAP, unfortunately the tools often returned inaccurate results as the application would get overloaded and return errors unrelated to the specific test being run by the tool.

A manual test found no SQL injection vulnerabilities, but we did find paths to XSS injection attacks in all user input that may show user supplied data on the website (3). During development we’ve also had vulnerable dependencies in our application and docker images, which was detected by security scanners (snyk, Github dependabot).

