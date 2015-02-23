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

Requesting ALL Player cards (Won from tricks)
```
CARDS? 
```

Requesting Current Suit in Play
```
CURRENT_SUIT?
```

Requesting Current Cards on Table
```
CURRENT_HAND?
```

Requesting All Player Scores (From won cards)
```
PLAYER_SCORES?
```

Request to Change Name of Player
```
SET_PLAYER_NAME APlayerName
```

Exit Game
```
QUIT
```



