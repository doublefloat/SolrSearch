# Search History SQL Statemets Design
### [Retrieving](#retriving)
```sql
--get default search
select id,query,time_range as timeRange,filters,fields,page,page_size as pageSize,default_search as defaultSearch
from history
where user_id=#{userId} and default_search=true

--timeRange is missed
select id,query,time_range as timeRange,filters,fields,page,page_size as pageSize,default_search as defaultSearch
from history
where user_id=#{userId} and status=#{status}
order by updated_time desc
limit 0,#{num}

--timeRange is present
select id,query,time_range as timeRange,filters,fields,page,page_size as pageSize,default_search as defaultSearch
from history
where userId=#{userID} and status=#{status} 
	and updated_time>=#{startTime} and updated_time<=#{endTime}
order by updated_time desc
limit 0,#{num}

```
### [Updating](#updating)
```sql
update history
/*
set query=#{query} and time_range=#{timeRange}
	fields=#{fields} and filters=#{filters} and
	page=#{page} and page_size=#{pageSize} and
	updated_time=CURRENT_TIMESTAMP
where id=#{id}
*/
create procedure update_history(
		in _id bigint,
		in _query varchar(500),
		in _time_range varchar(50),
		in _filters varchar(500),
		in _fields varchar(500),
		in _page int,
		in _page_size int)
begin
	declare rows int default 0;
	start transaction;
		if not _query is null
		then update history set query=_query where id=_id;
		set rows=rows+row_count();
		end if;
		
		if not _time_range is null
		then update history set time_range=_time_range where id=_id;
		set rows=rows+row_count();
		end if;

		if not _filters is null
		then update history set filters=_filters where id=_id;
		set rows=rows+row_count();
		end if;

		if not _fields is null
		then update history set fields=_fields where id=_id;
		set rows=rows+row_count();
		end if;

		if not _page is null
		then update history set page=_page where id=_id;
		set rows=rows+row_count();
		end if;

		if not _page_size is null
		then update history set page_size=_page_size where id=_id;
		set rows=rows+row_count();
		end if;
	commit;
	select rows; 
end;

--reset default search
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
### [Saving](#saving)
```sql
/*
--delete should be done before add new default serach
delete from history where id=#{id}
*/
--another way
delete from  history where user_id=#{userId} and default_search=true

--add new default search

insert into history(id,user_id,query,time_range,fields,filters,page,page_size,status,default_search,updated_time,created_time)
values(default,#{userId},#{query},#{timeRange},#{fields},#{filters},#{page},#{pageSize},'ACTIVE',#{defaultSearch},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)
```
### [Deleting](#deleting)
```sql
update history set status='DELETE' and updated_time=CURRENT_TIMESTAMP
where id=#{id}
```
