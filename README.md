# LeHearts_Server

The current server is operating [HERE](http://lehearts-env.elasticbeanstalk.com/)

## To Run LOCALLY:

Make sure you have Java 7, Eclipse and Tomcat Apache installed.  

```sh
1. Import the project to Eclipse
2. Add a Tomcat Apache server, and target the imported project 
3. Start the server 
```

Server should now be running on [localhost:8080/LeHearts_Server/](http://localhost:8080/LeHearts_Server/).

## Server Example Commands (Using Client)

Playing 2 of clubs
```sh
MOVE 2 : C
```

Requesting Player Hand
```
HAND?
```

Requesting Player cards (Won from tricks)
```
CARDS? 
```

Exit Game
```
QUIT
```



