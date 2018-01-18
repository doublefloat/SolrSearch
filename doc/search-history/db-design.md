### Table:history
```
+-----------------+----------------------------------+------+-----+-------------------+----------------+
| Field           | Type                             | Null | Key | Default           | Extra          |
+-----------------+----------------------------------+------+-----+-------------------+----------------+
| id              | bigint(20) unsigned              | NO   | PRI | NULL              | auto_increment |
| user_id         | bigint(20)                       | NO   | MUL | NULL              |                |
| search_name     | varchar(255)                     | NO   |     | NULL              |                |
| query           | varchar(500)                     | NO   |     | NULL              |                |
| collection      | varchar(100)                     | NO   |     | NULL              |                |
| aggregate_field | varchar(20)                      | YES  |     | NULL              |                |
| time_range      | varchar(50)                      | YES  |     | NULL              |                |
| time_type       | enum('COMMON','BACKDATE','USER') | YES  |     | NULL              |                |
| filters         | varchar(500)                     | YES  |     | NULL              |                |
| fields          | varchar(500)                     | YES  |     | NULL              |                |
| page            | int(11)                          | YES  |     | NULL              |                |
| page_size       | int(11)                          | YES  |     | NULL              |                |
| status          | enum('ACTIVE','DELETE')          | YES  |     | NULL              |                |
| default_search  | tinyint(1)                       | NO   |     | NULL              |                |
| updated_time    | datetime                         | NO   |     | CURRENT_TIMESTAMP |                |
| created_time    | datetime                         | NO   |     | CURRENT_TIMESTAMP |                |
+-----------------+----------------------------------+------+-----+-------------------+----------------+

```
```sql
CREATE TABLE history (
   id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
   user_id bigint(20) NOT NULL,
   search_name varchar(255) NOT NULL,
   query varchar(500) NOT NULL,
   collection varchar(100) NOT NULL,
   aggregate_field varchar(20),
   time_range varchar(50),
   time_type enum('COMMON','BACKDATE','USER'),
   filters varchar(500),
   fields varchar(500),
   page int,
   page_size int,
   status enum('ACTIVE','DELETE'),
   default_search BOOL NOT NULL,
   updated_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   created_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   
   PRIMARY KEY (id),
   INDEX(user_id),
   INDEX(user_id,status)
 )
```
