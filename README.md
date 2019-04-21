
# Piggy Metrics with Zipkin 

**A simple way to deal with personal finances**


Tools Required:

Java 8<br>
Maven 3.3.9<br>
IntelliJ<br>
Erlang (otp_win64_21.3)<br>
MongoDB (4.0.5)<br>
Robo 3t<br>
Rabbitmq (3.7.13)<br>

Please run below commands using MongoD CLI after MongoDB Installation:<br>

mongod --dbpath "C:\chiru\MongoDB\data" --logpath "C:\chiru\MongoDB\log\logs.txt" --install --serviceName "MongoDB"<br>
mongod --config D:\mongodb\mongo.config --install<br>
<br>
use account<br>
db.account.insert({"name":"tutorials point"})<br>
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"account"}]})

use authtest<br>
db.authtest.insert({"name":"tutorials point"})	<br>
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"authtest"}]})	

use notification<br>
db.notification.insert({"name":"tutorials point"})<br>
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"notification"}]})<br>

use statistics<br>
db.statistics.insert({"name":"tutorials point"})<br>
db.createUser(
{	user: "mongo",
	pwd: "mongo",
	roles:[{role: "userAdmin" , db:"statistics"}]})	<br>

Dump Mongdb Data into accounts db from mongodb module<br>

mongo localhost:27017/account C:\chiru\Micro_Services\piggymetrics-master\mongodb\dump\account-service-dump.js<br>

Rabbit Mq Start and stop:<br>

rabbitmq-plugins enable rabbitmq_management<br>
rabbitmqctl stop 	<br>
rabbitmq-server start<br>
