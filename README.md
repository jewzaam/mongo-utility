Mongo Utility
===============
The intent of this project is to provide some basic utility capabilities for working with mongo from java.

# License
This software is licensed under [GPLv3](http://www.gnu.org/licenses/gpl.txt)

# Configuration
This project uses (Hystrix)[https://github.com/Netflix/Hystrix] to wrap commands.  All hystrix commands use default values.  For a full documentation see the [Hystrix Configuration](https://github.com/Netflix/Hystrix/wiki/Configuration).  The command keys available for configuration are:
* MongoDrop
* MongoFind
* MongoIndex
* MongoUpsert

For example, to set the timeout for the MongoUpsert command to 2500 milliseconds add the following property to config.properties:
```text
hystrix.command.MongoUpsert.execution.isolation.thread.timeoutInMilliseconds=2500
```
