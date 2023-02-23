#mvn clean package
docker stop weatherbot
docker rm weatherbot
docker build -t weatherbot .
docker run --name weatherbot --network=nelf-network -p 127.0.0.1:7776:8080 weatherbot