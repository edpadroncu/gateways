# Gateways Managment REST-API
Managing gateways - master devices that control multiple peripheral devices

## Heroku Access
- base-url: <b>https://gateways-managment.herokuapp.com/</b>
- swagger-ui: <b>https://gateways-managment.herokuapp.com/swagger-ui.html</b>
- url download jar file(using mysql local db): <b>https://drive.google.com/drive/folders/1i5XtjpDzBFKxq4G1uYFNkG_aMVdp5rEg?usp=sharing</b>

## Important Notes:
1. The app has his own services description through Swagger(OpenAPI Specification). Which works also as a basic UI, in order to test de endpoints.
2. The app was deployed on heroku cloud.
3. The app has covered unit test for, Services and Controller files.
4. The app use by default Mysql, which create de database and tables by default. 
- H2 and Postgres where tested too, connection config are on 'application.properties' files. 
- In case of use Postgres connection, please create de database 'gateways' manually.

##Available Actions. Required to be in root folder:
- Run Spring Boot app with maven: <b>mvn spring-boot:run</b>
- Build jar file: <b>mvn package spring-boot:repackage</b>
- Run Spring Boot app with java, after build jar: <b>java -jar target\gateways-0.0.1-SNAPSHOT.jar</b>

##Deploy on Amazon. Required to be in root folder:
- Generate docker image: <b>mvn clean install</b>
- Upload docker image to gcloud: <b>gcloud docker -- push gateways:latest</b>
- Upload deployment file: <b>envsubst < k8s/deployment.yml | kubectl apply -f -</b>
- Upload service file: <b>envsubst < k8s/service.yml | kubectl apply -f -</b>
- Find out if pod is running: <b>kubectl get pods | grep gateways</b>
- Run locally the pod: <b>kubectl port-forward gateways-5f88cccb65-7455h 8081:8080</b>


