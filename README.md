# Bulletin

## Build and Run ##

Windows
```
.\ps-init.ps1
```

Linux
```
./unix-init.sh
```

## Hit Endpoints Locally ##
http://localhost:8080/bulletin/api

## Connect the database (Docker Container: bin/db) ##
```
psql -h localhost -p 5432 -U compose-postgres -W
```

## Push App to Heroku ##
```
heroku login
heroku container:login
heroku config:set JAVA_TOOL_OPTIONS="-Xmx300m" --app="bulletin-backend-app"
heroku container:push --app="bulletin-backend-app" web
heroku container:release web --app="bulletin-backend-app"
```

## Kill all connections to Heroku Bulletin DB ##
```
heroku pg:killall --app="bulletin-backend-app"
```

## Requirements ##
You will need <code>config.properties</code> to run this software
Make a request to me for access.