# To run my project

- go to project path and run command `docker-compose up --build`

## example curl

#### EX01:

`
$ curl --location 'http://localhost:8888/admin/lotteries' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
--data '{
"ticket": "000001",
"price": 200,
"amount": 1
}'
`

#### EX02:

`
$ curl --location 'http://localhost:8888/lotteries'
`

#### EX03:

`
$ curl --location --request POST 'http://localhost:8888/users/0000000001/lotteries/000001'
`

#### EX04:

`
$ curl --location 'http://localhost:8888/users/0000000001/lotteries'
`

#### EX05:

`
$ curl --location --request DELETE 'http://localhost:8888/users/0000000009/lotteries/000001'
`