version?=v1
replicas?=1
app=akka-cluster-client-demo-frontend

package:
	gradle clean build

create-image:
	docker build -t ${app} -f Dockerfile .

create-tag:
	docker tag ${app}:latest ${app}:${version}	
	
deploy-to-kube:
	kubectl create -f kube/deployment.yml
	
delete-deploy:
	kubectl delete deployment/${app}
				
update-image:	
	kubectl set image deployment/${app} ${app}=${app}:${version}	
	
scale:
	kubectl scale deployment/${app} --replicas=${replicas}