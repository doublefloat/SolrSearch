### Deployment Guide
---

#### Ansible playbook

* Create user searcher
* Assign user search remote communicate and execute authorities.
* Migrate deployment scripts and initialize basic work directories.
* Remote executing ops scripts including startup/shutdown/restart scripts.

#### Using password as access token

if the public key has not access by the remote server, use '--ask-pass' keyword and to remove connection with ssh type under host connection building module.

* example:

```
# ansible-playbook example.yml -i hosts --ask-pass

```


#### Environment init 

```
# ansible-playbook ${deployment}/ansible/env_init.yml -i hosts

```

#### Start up search engine application 

```
# ansible-playbook ${deployment}/ansible/start_up.yml -i hosts

```

#### Shut down search engine application 

```
# ansible-playbook ${deployment}/ansible/shut_down.yml -i hosts

```

#### Restart search engine application 

```
# ansible-playbook ${deployment}/ansible/restart.yml -i hosts

```
