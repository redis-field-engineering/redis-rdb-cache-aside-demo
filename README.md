# redis-rdb-cache-aside-demo
[Cache-Aside](https://redis.com/solutions/use-cases/caching/#:~:text=Cache%2Daside%20(Lazy%2Dloading)) caching with [Redis Enterprise](https://redis.com/redis-enterprise-software/overview/) and RDBMS [TestContainers](https://www.testcontainers.org/)

## Overview
A demo project to showcase Cache-Aside caching pattern using Redis Enterprise TestContainer and few popular RDBMS. Currently, this project is built with MSSQL, MySQL, Oracle (using pre-built 12c and 19c docker images) and Postgres TestContainers.

## Prerequisites
* [Git](https://git-scm.com/) 2.2.1 or later
* JDK 11 or later e.g. [Azul OpenJDK](https://www.azul.com/downloads/?package=jdk#download-openjdk)
* [Apache Maven](https://maven.apache.org/index.html) 3.6.3 or later
* [Docker](https://docs.docker.com/get-docker) 1.9 or later

## Usage
First clone the Git repository:
```bash
git clone https://github.com/redis-field-engineering/redis-rdb-cache-aside-demo.git
```
Then run the Tests:
### MSSQLTest
```bash
mvn -Dtest=MSSQLTest clean test
```
Or with images of your choice
```bash
mvn -Dtest=MSSQLTest -Dredis.image.name=redislabs/redis:6.2.8-50 -Dmssql.image.name=mcr.microsoft.com/mssql/server:2019-latest clean test
```
<details><summary>Expected Output:</summary>
<p>

```bash
[INFO] ------------< com.redis.caching:redis-rdb-cache-aside-demo >------------
[INFO] Building Redis Enterprise Cache Aside Demo 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
....
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.redis.caching.rdb.MSSQLTest
....
21:45:33.912 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Running beforeAll
21:45:33.915 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Starting Redis Image: redislabs/redis:latest
21:46:38.385 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home /redis container is running now
21:46:38.387 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Starting MSSQL Image: mcr.microsoft.com/mssql/server:2017-latest
21:47:00.452 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home /mssql container is running now
21:47:00.481 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Redis Database is up! Now testing PING command..
21:47:01.617 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Total Redis connection test time: PT1.135125S
21:47:01.638 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home MSSQL Database is up! Now testing JDBC URL and Test Query..
21:47:01.638 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home JDBC URL: jdbc:sqlserver://localhost:55178
21:47:01.638 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Query String: SELECT 1
21:47:01.661 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Total MSSQL connection test time: PT0.02292S
21:47:01.906 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Creating RedisConnect database
21:47:02.210 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Creating EMP table
21:47:02.222 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Inserting a record into EMP table
21:47:02.238 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home emp 1 record in MSSQL
21:47:02.243 [main] INFO  cache-aside-demo - empno : 1
21:47:02.244 [main] INFO  cache-aside-demo - fname : Virag
21:47:02.245 [main] INFO  cache-aside-demo - lname : Tripathi
21:47:02.245 [main] INFO  cache-aside-demo - job : PFE
21:47:02.245 [main] INFO  cache-aside-demo - mgr : 19
21:47:02.245 [main] INFO  cache-aside-demo - hiredate : 2018-08-05 04:07:50.0
21:47:02.246 [main] INFO  cache-aside-demo - sal : 90101.3400
21:47:02.246 [main] INFO  cache-aside-demo - comm : 1235.1300
21:47:02.246 [main] INFO  cache-aside-demo - dept : 96
21:47:02.285 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Employee 1 not found in Redis
21:47:02.325 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Employee 1 found in MSSQL
21:47:02.325 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home MSSQL Query time: 40 ms
21:47:02.325 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Caching Employee 1 in Redis
21:47:02.340 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Employee 1 record in Redis {fname=Virag, lname=Tripathi, comm=1235.13, mgr=19, empno=1, dept=96, job=PFE, hiredate=2018-08-05, sal=90101.34}
21:47:02.340 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home Redis Query time: 15 ms
21:47:02.343 [main] INFO  cache-aside-demo - Instance: 59935@ViragTrathisMBP.home running afterAll
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 88.996 s - in com.redis.caching.rdb.MSSQLTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:35 min
[INFO] Finished at: 2022-01-17T21:47:02-05:00
[INFO] ------------------------------------------------------------------------
```
</p>
</details>

### MySQLTest
```bash
mvn -Dtest=MySQLTest clean test
```
Or with images of your choice
```bash
mvn -Dtest=MySQLTest -Dredis.image.name=redislabs/redis:6.2.8-50 -Dmysql.image.name=mysql:5.7.33 clean test
```
<details><summary>Expected Output:</summary>
<p>

```bash
[INFO] ------------< com.redis.caching:redis-rdb-cache-aside-demo >------------
[INFO] Building Redis Enterprise Cache Aside Demo 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
....
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.redis.caching.rdb.MySQLTest
....
21:48:35.381 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Running beforeAll
21:48:35.385 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Starting Redis Image: redislabs/redis:latest
21:49:45.607 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home /redis container is running now
21:49:45.608 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Starting MySQL Image: mysql:latest
21:50:21.539 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home /mysql container is running now
21:50:21.590 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Redis Database is up! Now testing PING command..
21:50:22.485 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Total Redis connection test time: PT0.89474S
21:50:22.500 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home MySQL Database is up! Now testing JDBC URL and Test Query..
21:50:22.500 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home JDBC URL: jdbc:mysql://localhost:55181/RedisConnect
21:50:22.500 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Query String: SELECT 1
21:50:22.505 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Total MySQL connection test time: PT0.005193S
21:50:22.970 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Creating EMP table
21:50:23.038 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Inserting a record into EMP table
21:50:23.077 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home emp 1 record in MySQL
21:50:23.082 [main] INFO  cache-aside-demo - empno : 1
21:50:23.082 [main] INFO  cache-aside-demo - fname : Virag
21:50:23.083 [main] INFO  cache-aside-demo - lname : Tripathi
21:50:23.083 [main] INFO  cache-aside-demo - job : PFE
21:50:23.083 [main] INFO  cache-aside-demo - mgr : 19
21:50:23.085 [main] INFO  cache-aside-demo - hiredate : 2018-08-05 04:07:50
21:50:23.085 [main] INFO  cache-aside-demo - sal : 90101.3400
21:50:23.085 [main] INFO  cache-aside-demo - comm : 1235.1300
21:50:23.085 [main] INFO  cache-aside-demo - dept : 96
21:50:23.126 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Employee 1 not found in Redis
21:50:23.169 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Employee 1 found in MySQL
21:50:23.169 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home MySQL Query time: 43 ms
21:50:23.169 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Caching Employee 1 in Redis
21:50:23.188 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Employee 1 record in Redis {fname=Virag, lname=Tripathi, comm=1235.13, mgr=19, empno=1, dept=96, job=PFE, hiredate=2018-08-05, sal=90101.34}
21:50:23.188 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home Redis Query time: 19 ms
21:50:23.193 [main] INFO  cache-aside-demo - Instance: 60028@ViragTrathisMBP.home running afterAll
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 108.414 s - in com.redis.caching.rdb.MySQLTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:54 min
[INFO] Finished at: 2022-01-17T21:50:23-05:00
[INFO] ------------------------------------------------------------------------
```
</p>
</details>

### OracleTest
```bash
mvn -Dtest=OracleTest clean test
```
Or with images of your choice
```bash
mvn -Dtest=OracleTest -Dredis.image.name=redislabs/redis:6.2.8-50 -Doracle.image.name=virag/oracle-19.3.0.0.1-ee:latest clean test
```
<details><summary>Expected Output:</summary>
<p>

```bash
[INFO] ------------< com.redis.caching:redis-rdb-cache-aside-demo >------------
[INFO] Building Redis Enterprise Cache Aside Demo 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
....
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.redis.caching.rdb.OracleTest
....
21:56:00.329 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Running beforeAll
21:56:00.333 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Starting Redis Image: redislabs/redis:latest
21:56:57.988 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home /redis container is running now
21:56:57.992 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Starting Oracle Image: virag/oracle-12.2.0.1-ee:latest
21:57:45.319 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home /oracle container is running now
21:57:45.330 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home /oracle is running now
21:57:45.466 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Redis Database is up! Now testing PING command..
21:57:47.927 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Total Redis connection test time: PT2.460244S
21:57:47.958 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Oracle Database is up! Now testing JDBC URL and Test Query..
21:57:47.960 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home JDBC URL: jdbc:oracle:thin:@localhost:55184/ORCLPDB1
21:57:47.960 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Query String: SELECT 1 FROM DUAL
21:57:48.024 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Total Oracle connection test time: PT0.063322S
21:57:51.316 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Creating EMP table
21:57:52.006 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Inserting a record into EMP table
21:57:52.869 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Employee 1 record in Oracle
21:57:52.912 [main] INFO  cache-aside-demo - EMPNO : 1
21:57:52.912 [main] INFO  cache-aside-demo - FNAME : Virag
21:57:52.912 [main] INFO  cache-aside-demo - LNAME : Tripathi
21:57:52.913 [main] INFO  cache-aside-demo - JOB : PFE
21:57:52.913 [main] INFO  cache-aside-demo - MGR : 19
21:57:52.913 [main] INFO  cache-aside-demo - HIREDATE : 2018-08-05 04:07:50
21:57:52.913 [main] INFO  cache-aside-demo - SAL : 90101.34
21:57:52.913 [main] INFO  cache-aside-demo - COMM : 1235.13
21:57:52.913 [main] INFO  cache-aside-demo - DEPT : 96
21:57:52.972 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Employee 1 not found in Redis
21:57:53.297 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Employee 1 found in Oracle
21:57:53.297 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Oracle Query time: 325 ms
21:57:53.297 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Caching Employee 1 in Redis
21:57:53.330 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Employee 1 record in Redis {fname=Virag, lname=Tripathi, comm=1235.13, mgr=19, empno=1, dept=96, job=PFE, hiredate=2018-08-05, sal=90101.34}
21:57:53.331 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home Redis Query time: 34 ms
21:57:53.343 [main] INFO  cache-aside-demo - Instance: 60221@ViragTrathisMBP.home running afterAll
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 113.657 s - in com.redis.caching.rdb.OracleTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:59 min
[INFO] Finished at: 2022-01-17T21:57:53-05:00
[INFO] ------------------------------------------------------------------------
```
</p>
</details>

### PostgresTest
```bash
mvn -Dtest=PostgresTest clean test
```
Or with images of your choice
```bash
mvn -Dtest=PostgresTest -Dredis.image.name=redislabs/redis:6.2.8-50 -Dpostgres.image.name=postgres:12.7 clean test
```
<details><summary>Expected Output:</summary>
<p>

```bash
[INFO] ------------< com.redis.caching:redis-rdb-cache-aside-demo >------------
[INFO] Building Redis Enterprise Cache Aside Demo 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
....
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.redis.caching.rdb.PostgresTest
....
22:00:14.416 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Running beforeAll
22:00:14.420 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Starting Redis Image: redislabs/redis:latest
22:01:22.831 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home /redis container is running now
22:01:22.833 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Starting Postgres Image: postgres:12.5
22:01:30.241 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home /postgres container is running now
22:01:30.347 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Redis Database is up! Now testing PING command..
22:01:32.617 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Total Redis connection test time: PT2.269497S
22:01:32.634 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Postgres Database is up! Now testing JDBC URL and Test Query..
22:01:32.636 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home JDBC URL: jdbc:postgresql://localhost:55186/RedisConnect?loggerLevel=OFF
22:01:32.636 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Query String: SELECT 1
22:01:32.644 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Total Postgres connection test time: PT0.008124S
22:01:33.159 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Creating EMP table
22:01:33.180 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Inserting a record into EMP table
22:01:33.197 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home emp 1 record in Postgres
22:01:33.201 [main] INFO  cache-aside-demo - empno : 1
22:01:33.201 [main] INFO  cache-aside-demo - fname : Virag
22:01:33.201 [main] INFO  cache-aside-demo - lname : Tripathi
22:01:33.201 [main] INFO  cache-aside-demo - job : PFE
22:01:33.201 [main] INFO  cache-aside-demo - mgr : 19
22:01:33.201 [main] INFO  cache-aside-demo - hiredate : 2018-08-05
22:01:33.202 [main] INFO  cache-aside-demo - sal : 90101.3400
22:01:33.202 [main] INFO  cache-aside-demo - comm : 1235.1300
22:01:33.202 [main] INFO  cache-aside-demo - dept : 96
22:01:33.255 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Employee 1 not found in Redis
22:01:33.310 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Employee 1 found in Postgres
22:01:33.357 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Postgres Query time: 102 ms
22:01:33.358 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Caching Employee 1 in Redis
22:01:33.381 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Employee 1 record in Redis {fname=Virag, lname=Tripathi, comm=1235.13, mgr=19, empno=1, dept=96, job=PFE, hiredate=2018-08-05, sal=90101.34}
22:01:33.381 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home Redis Query time: 23 ms
22:01:33.386 [main] INFO  cache-aside-demo - Instance: 60412@ViragTrathisMBP.home running afterAll
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 79.766 s - in com.redis.caching.rdb.PostgresTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:29 min
[INFO] Finished at: 2022-01-17T22:01:33-05:00
[INFO] ------------------------------------------------------------------------
```
</p>
</details>

## Logs
Detailed logs are created under `logs/redis-rdb-cache-aside-demo.log`