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
heroku stack:set container
heroku stack:set container --app bulletin-backend-app
```

## Kill all connections to Heroku Bulletin DB ##
```
heroku pg:killall --app="bulletin-backend-app"
```

## Requirements ##
You will need <code>.env</code> to run this software.