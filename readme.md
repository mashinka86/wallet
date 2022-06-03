### Run application wallet

1. In command line in directory app run command: 
```
    gradle build
```    
2. After that to build image in main directory wallet run command:
````
      docker build -t wallet .
````
3. And then to run infrastructure with application run command:
````
   docker-compose -f docker-compose.yml up -d
````
About application.xml:

````
spring:
   application:
      name: wallet #name of application
      data.mongodb:
         host: localhost #host of mongo
         port: 27017 #port of mongo
         database: wallet # database name

server.port: 8888 # application port

kafka:
   bootstrapAddress: localhost:9092 #kafka address
   topic: wallet.transactions #kafka topic
   group: transactions-1 #kafka group

````


       
