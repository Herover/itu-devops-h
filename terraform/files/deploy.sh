ROOT_PASSWORD=$1
WEB_HOSTS=$2
DRONE_BUILD_NUMBER=$3
DOCKER_USERNAME=$4
DOCKER_PASSWORD=$5
POSTGRES_PASSWORD=$6

maxtries=10
delay=5

for WEB_HOST in $WEB_HOSTS
do
    echo "Updating $WEB_HOST..."
    sshpass -p "$ROOT_PASSWORD" scp -o StrictHostKeyChecking=no terraform/files/update_image.sh root@$WEB_HOST:/tmp/
    sshpass -p "$ROOT_PASSWORD" ssh -o StrictHostKeyChecking=no root@$WEB_HOST chmod +x /tmp/update_image.sh
    sshpass -p "$ROOT_PASSWORD" ssh -o StrictHostKeyChecking=no root@$WEB_HOST \
        /tmp/update_image.sh \
            registry.digitalocean.com/minitwit/minitwit-server \
            $DRONE_BUILD_NUMBER \
            $DOCKER_USERNAME \
            $DOCKER_PASSWORD \
            $POSTGRES_PASSWORD

    echo "Testing server"
    r="NO"
    tries=0
    while [ $r != "OK" ] && [ $tries -lt $maxtries]
    do
        sleep $delay
        echo Testing...
        # If $r is empty then the test will fail, so echo NO into it if server is down
        r=$(curl -s http://127.0.0.1:8080/status || echo "NO")
    done

    if [ $tries -eq $maxtries ]
    then
        echo "Service on $WEB_HOST is not available after $tries tries!"
        exit 1
    else
        echo "Done updating $WEB_HOST..."
    fi
done

echo "Updated all servers"


