
This is a guide that explains how to run the service. 

1) first you need to clone the code from github:  
https://github.com/crennui/query-service-exercise

2) cd into the folder you just cloned (already contains the Dockerfile)
and run the following docker commands: 

docker build -t query_service:v1

docker images  (after this command find the image you just created and copy it's id)

replace the <LOCAL_PATH> with the path to your results folder (should be inside local folder).
This is where your results file will be saved.
And replcae the <ID> with the docker image id you coppied from last command. 

docker run -p 8080:8080 -v <LOCAL_PATH>:/query-service-exercise/results <ID>

After this the service should be runing (contact me if there are any problems)
//-------------------------------------------------------------------------------

how to check it is up and ok ? 
you can send a get request to:
http://localhost:8080/query and you will get a short message (you can do it through chrome or postman)

how to send a query ? 
to send a query you need to send a post request to "http://localhost:8080/query" (I would suggest using Postman)
with a body that contains a json, and example query: 

{
    "query" : "select * from Rna limit 10",
    "fileName" : "testdata.csv"
}



notes: 
* inside the resources folder there is the application.conf file, there you can change 
  the PostgreSql connection info and service properties. 
* if there is any problem please contact me. 
* I think it is worth mentioning that most of the technologies in this project are new to me
  so they are probably some best-practices and things I could do better. 
* They are some more things I would add to this projcts like testings ... but 
  I know the main objective here is to see that I am capable of working ... so 
  It is not a production ready code.   

