# Gateways Managment REST-API
Managing gateways - master devices that control multiple peripheral devices

## Important Notes:
1. The app has his own services description through Swagger(OpenAPI Specification)
2. The app was deployed on heroku
3. The app has covered unit test for, Services and Controller files.

## Heroku Access
- base-url: <b>https://gateways-managment.herokuapp.com/</b>
- swagger-ui: <b>https://gateways-managment.herokuapp.com/swagger-ui.html</b>

##Actions. Required to be in root folder:
- Execute app with maven: <b>mvn spring-boot:run</b>
- Generate docker image: <b>mvn clean install</b>
- Upload docker image to gcloud: <b>gcloud docker -- push gateways:latest</b>

##Deploy on Amazon
- Upload deployment file: <b>envsubst < k8s/deployment.yml | kubectl apply -f -</b>
- Upload service file: <b>envsubst < k8s/service.yml | kubectl apply -f -</b>
- Find out if pod is running: <b>kubectl get pods | grep gateways</b>
- Running locally the gcloud pod: <b>kubectl port-forward gateways-5f88cccb65-7455h 8081:8080</b>


