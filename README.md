## pgtask - A Sample RESTful Webservice 
`pgtask` is a basic webservice built with [Micronaut](https://docs.micronaut.io/3.8.4/guide/index.html) to provide
sample data from StackExchange API. 

### Docker for Database
The application comes with a docker compose file to make it easy to use Postgresql, you will need `docker` in 
your environment to run (symbol `%` is the shell prompt):
```shell
% docker compose up -d 
[+] Running 1/1
 ⠿ Container pgtask-db-1  Started           
```
Verify that the container is up and running by:
```shell
% pgtask % docker ps 
CONTAINER ID   IMAGE                  COMMAND                  CREATED        STATUS          PORTS                                               NAMES
6d3f8bc5dd41   postgres:14.1-alpine   "docker-entrypoint.s…"   30 hours ago   Up 31 seconds   0.0.0.0:18543->5432/tcp                             pgtask-db-1

```
### Running
To compile and start the service, use:
```shell
./gradlew run 
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
  Micronaut (v3.8.4)

16:38:15.523 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
16:38:15.640 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
16:38:15.643 [main] INFO  i.m.l.AbstractLiquibaseMigration - Running migrations for database with qualifier [default]
Feb 10, 2023 4:38:15 PM liquibase.database
INFO: Set default schema name to public
Feb 10, 2023 4:38:15 PM liquibase.changelog
INFO: Reading from public.databasechangelog
16:38:16.285 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 1039ms. Server Running: http://localhost:8080
16:38:17.100 [main] INFO  ccb.pgames.services.QuestionService - Cleared questions from DB: 20
16:38:17.185 [main] INFO  ccb.pgames.services.QuestionService - Fetched and persisted questions from DB: 20
```

On every start it will fetch 20 questions from StackExchange and persist them in the database.

## Sample of Usage
For the `/questions` endpoint:
- To get all the questions in the DB
```shell
% curl -q 'http://localhost:8080/questions'
[
  {
    "id": 741,
    "title": "How to perform multiple array filtering",
    "tags": [
      "javascript",
      "angular",
      "typescript"
    ],
    "is_answered": false,
    "view_count": 72,
    "answer_count": 1,
    "creation_date": "2023-02-08T14:33:04",
    "user_id": 11346489
  },
 ...
 ...
 ]
```
- To get only one question by its ID:
```shell
 % curl -q 'http://localhost:8080/questions/742' | jq  
{
  "id": 742,
  "title": "WKWebView: how to display a big HTML5 canvas?",
  "tags": [
    "canvas",
    "wkwebview"
  ],
  "is_answered": false,
  "view_count": 11,
  "answer_count": 0,
  "creation_date": "2023-02-08T14:31:51",
  "user_id": 359570
}
```
- To delete a question by id:
```shell
 % curl -v -X DELETE 'http://localhost:8080/questions/779' 
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> DELETE /questions/779 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.79.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 204 No Content
< date: Fri, 10 Feb 2023 15:55:44 GMT
< connection: keep-alive
< 
* Connection #0 to host localhost left intact
```
- To search for questions that have specific tags, use the query string variable `tags` with all the tag values joined 
by comma:
```shell
% curl  'http://localhost:8080/questions?tags=angular,python' | jq
[
  {
    "id": 761,
    "title": "How to perform multiple array filtering",
    "tags": [
      "vue.js",
      "angular",
      "typescript"
    ],
    "is_answered": false,
    "view_count": 72,
    "answer_count": 1,
    "creation_date": "2023-02-08T14:33:04",
    "user_id": 11346489
  },
  {
    "id": 766,
    "title": "Module Warning Angular Material themes should be created from a map containing the keys color, typography, density",
    "tags": [
      "angular",
      "sass",
      "angular-material",
      "angular15"
    ],
    "is_answered": false,
    "view_count": 115,
    "answer_count": 1,
    "creation_date": "2023-02-08T12:11:14",
    "user_id": 7620780
  },
  {
    "id": 770,
    "title": "Serve TailwindCSS with django_plotly_dash",
    "tags": [
      "python",
      "css",
      "django",
      "tailwind-css",
      "plotly-dash"
    ],
    "is_answered": false,
    "view_count": 50,
    "answer_count": 2,
    "creation_date": "2023-02-08T03:50:52",
    "user_id": 4183877
  }
]

## Packaging and deployment
To build a JAR file for deployment:
```shell
 % ./gradlew shadowJar

BUILD SUCCESSFUL in 500ms
3 actionable tasks: 3 up-to-date
```
The JAR file is created in `./build/libs/pgtask-0.1-all.jar`

## About Micronaut and the libraries used 
- [API Reference](https://docs.micronaut.io/3.8.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.8.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
- [Micronaut Liquibase Database Migration documentation](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html)
- [https://www.liquibase.org/](https://www.liquibase.org/)
- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
