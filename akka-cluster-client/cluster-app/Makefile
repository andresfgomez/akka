version?=v1

package:
	gradle clean shadowJar

create-image:
	docker build -t akka-cluster-client-demo-backend -f Dockerfile .

create-tag:
	docker tag akka-cluster-client-demo-backend:latest akka-cluster-client-demo-backend:${version}	
	
deploy-to-kube:
	kubectl create -f kube/deployment.yml
	
delete-deploy:
	kubectl delete deployment/akka-cluster-client-demo-backend
	
create-service:
	kubectl create -f kube/service.yml		
	
delete-service:
	kubectl delete service/akka-cluster-client-demo-backend
	
update-image:	
	kubectl set image deployment/akka-cluster-client-demo-backend akka-cluster-client-demo-backend=akka-cluster-client-demo-backend:${version}	