To start this service, execute the below command (maven required):

mvn spring-boot:run

SHOW ALL UPLOADED
The base link to show url's of all images successfully uploaded to imgur: http://localhost:8080/v1/images  (GET)

NEW UPLOAD JOB
POST request 
http://localhost:8080/v1/images/upload

request body
{
    "urls":[
            "http://www.google.com"
    ]
}

response body
{
    "jobId" : "1"
}
