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

#License
The MIT License (MIT)

Copyright (c) 2015 Cheng Peng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

