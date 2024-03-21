./mvnw spring-boot:build-image "-Dspring-boot.build-image.imageName=bernhardwinner/config-server-ms"
docker push bernhardwinner/config-server-ms