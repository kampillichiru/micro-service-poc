
# Piggy Metrics with Zipkin 

**A simple way to deal with personal finances**


Tools Required:

Java 8
Maven 3.3.9
IntelliJ
Erlang (otp_win64_21.3)
MongoDB (4.0.5)
Robo 3t
Rabbitmq (3.7.13)

Please run below commands using MongoD CLI after MongoDB Installation

mongod --dbpath "C:\chiru\MongoDB\data" --logpath "C:\chiru\MongoDB\log\logs.txt" --install --serviceName "MongoDB"
mongod --config D:\mongodb\mongo.config --install

use account
db.account.insert({"name":"tutorials point"})
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"account"}]})

use authtest
db.authtest.insert({"name":"tutorials point"})	
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"authtest"}]})	

use notification
db.notification.insert({"name":"tutorials point"})
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"notification"}]})

use statistics
db.statistics.insert({"name":"tutorials point"})
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"statistics"}]})	

Dump Mongdb Data into accounts db from mongodb module

mongo localhost:27017/account C:\chiru\Micro_Services\piggymetrics-master\mongodb\dump\account-service-dump.js

Rabbit Mq Start and stop:

rabbitmq-plugins enable rabbitmq_management
rabbitmqctl stop 	
rabbitmq-server start
