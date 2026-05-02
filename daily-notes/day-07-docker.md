### Docker

Docker is a platform for developing, shipping, and running applications in isolated environments called containers.

Container is a lightweight, standalone, executable package of software that includes everything needed to run an application: code, runtime, system tools, system libraries, and settings.

### Dockerfile

A Dockerfile is a text document that contains a set of instructions for building a Docker image.    

### Docker Image    
Image is a read-only template that contains the application code, runtime, libraries, and dependencies needed to run an application. Images are created from Dockerfiles.   

Docker Commands    

docker pull <image_name> - It is used to pull an image from the Docker registry    
docker images - It is used to list all the images    
docker run  <image_name>   
docker run -it <image_name>
docker start <container_name> or <container_id> - It is used to start a container    
docker stop <container_name> or <container_id> - It is used to stop a container    
docker rm <container_name> or <container_id> - It is used to remove a container    
docker rmi <image_name> or <image_id> - It is used to remove an image    

docker build -t <image_name> <path_to_dockerfile> - It is used to build an image from a Dockerfile    
docker run -d -p <host_port>:<container_port> <image_name> - It is used to run a container from an image    
docker ps - It is used to list all the running containers    
docker ps -a - It is used to list all the containers    
docker exec -it <container_name> <command> - It is used to run a command in a container    
docker logs <container_name> or <container_id> - It is used to view the logs of a container    
docker stats - It is used to view the stats of a container    
docker system prune - It is used to remove all the unused containers, images, and networks    
