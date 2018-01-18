# API Design of Search History
### [Retrieving](#retriving)
* url:/solr/history/retrieve
* method:GET
* request parameters:
	* userId:Long(not null)
	* offset:Long(*default*:0)
	* num:Long(*default*:20)
	* status:Enum('ACTIVE','DELETE')(*default*:ACTIVE)
* response format:

```
{
	"code":String,//when success
	"result":{
	  "params":{
		"userId":Long,
		"num":Long,
		"timeRange":String,
		"status":Enum("ACTIVE","DELETE")
	    },
	  "items":[{
		"id":Long,
		"query":String,
		"searchName":String,
		"aggregateField":String,
		"filters":String ,
		"fields":String,
		"timeRange":String,
		"timeType":String,
		"page":Integer,
		"pageSize":Integer,
		"defalutSearch":Boolean
	  }]
	}
}

{
	"code":String,//when error
	"params":{
		"userId":Long,
		"num":Long,
		"timeRange":String,
		"status":Enum("ACTIVE","DELETE")
	},
	"message":String
}
```
* response example:

### [Saving](#saving)
* url:/solr/history/save
* method:GET
* request parameters:
	* userId:String(not null)
	* query:String(not null)
  * collection:String(not null)
  * searchName:String(not null)	
  * aggregateField:String
	* timeRange:String
	* timeType:String
	* filters:String
	* fields:String
	* page:Integer
	* pageSize:Integer
	* defaultSearch:Boolean
* response format:
```
{
	"code":String,
	"params":{
		"userId":Long,
		"query":String,
		"collection":String,
		"aggregateField":String,
		"searchName":String,
		"timeRange":String,
		"timeType":String,
		"filters":String,
		"filelds":String,
		"page":Integer,
		"pageSize":Integer,
		"defaultSearch":Boolean
	},
	"message":String
}
```
* response example:
### [Updating](#updating)
* url:/solr/history/update
* method:GET
* request parameter:
	* id:Long(*not null*)
	* query:String
	* collection:String
	* searchName:String
	* aggregateField:String
	* timeRange:String
	* timeType:String
	* fields:String
	* filters:String
	* page:Integer
	* pageSize:Integer
* response format:
```
{
	"code":String,
	"params":{
		"id":Long,
		"query":String,
		"timeRange":String,
		"timeType":String,
		"filters":String,
		"filelds":String,
		"page":Integer,
		"pageSize":Integer
	},
	"message":String
}
```
* response example:
### [RESET DEFAULT SEARCH](#reset)
* url:/solr/history/default 
* method:GET
* request parameter:
	* id:Long(not null)
* response format:
```
{
	"code":String,
	"params":{
		"id":Long
	},
	"message":String
}
```
### [DELETING](#deleting)
* url:/solr/history/delete
* method:GET
* request paramters:
	* id:Long(*not null*)
* response format:
```
{
	"code":String,
	"params":{
		"id":Long
	},
	"message":String
}
```

