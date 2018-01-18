```sql
create database search_history;

create user 'solr-search'@'localhost' identified by 'solr-search@MYSQL2017';

grant all on search_history.* to 'solr-search'@'localhost';

use search_history;

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
 
 create procedure set_default_search(
 		in para bigint)
 begin
 	declare uid bigint;
 	declare rows int default 0;
 
 	select distinct user_id into uid 
 	from history 
 	where id=para;
 
 	start transaction;
 	update history
 	set default_search=false
 	where user_id=uid and default_search=true;
 	set rows=rows+(select row_count());
 	
 	update history
 	set default_search=true
 	where id=para;
 	set rows=rows+(select row_count());
 	
 	commit;
 	select rows;
 end;
```