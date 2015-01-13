# LeHearts_Server

The current server is operating [HERE](https://lehearts.herokuapp.com/)

## To Run LOCALLY:

Make sure you have Java and Maven installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ mvn install
$ foreman start web
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Server Essentials

This server will handle the web requests from the client, saving the state of the game in a database.

Server will make sure the request from client is LEGAL (passes rules), and send back a response.




