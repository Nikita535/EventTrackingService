Проект в лабораторию РТУ МИРЭА

Технологии:

```
- Java
- Spring
- Hibernate
- Swagger
- PostgreSql
- Lombok
- Docker
```
  API:
```
Метод логина
POST api/auth/login
body: {
    "username":{username},
    "email":{email},
    "password":"{password}"
}

Метод регистрации
POST api/auth/register
body: {
    "username":{username},
    "email":{email},
    "password":"{password}"
}

Метод получения информации о авторизованном пользователе
headers: {
    "Authorization": {access_token}
}
GET api/auth/login/userinfo

Метод изменения данных учетной записи
POST api/user/edit
headers: {
    "Authorization": {access_token}
}
body: {
    "username":{username},
    "email":{email},
    "password":"{password}"
}

Метод удаления пользователя
POST api/user/deleteUser?id={id}
headers: {
    "Authorization": {access_token}
}
param:{
    Long id
}

Метод регистрации на мероприятия
POST api/user/register?tag={tag}
headers: {
    "Authorization": {access_token}
}
param:{
    String tag
}

Метод получения всех мероприятий
GET api/event/getAll
headers: {
    "Authorization": {access_token}
}

Метод добавления события 
POST api/event/addEvent
headers: {
    "Authorization": {access_token}
}
body:{
    "title":"title",
    "description":"description",
    "location":"location",
    "tag":"tag"
}

Метод удаления мероприятия 
POST api/event/deleteEvent
headers: {
    "Authorization": {access_token}
}
body:{
    "tag":"tag"
}

Метод просмотра всех участников опредленных событий
POST api/event/checkEventMembers?tag={tag}
headers: {
    "Authorization": {access_token}
}
param:{
    String tag
}

URL документации swagger: /swagger-ui/index.html#/
А также документация в каталоге Docs в корне проекта
```
Для запуска проекта:
```
1. Установить Docker с оффициального сайта https://www.docker.com/
2. Выбрать папку, где будет храниться проект
3. В выбранной папке прописать команду "git clone https://github.com/Nikita535/EventTrackingService"
4. В терминале (в корневой папке проекта) прописать команду docker-compose up
```