# Solr Search API design
## Requirements:
### Search
* description:
  Get related index information
* path:/solr/search/request
* method:GET
* request parameters:
  * collection:String(required)
  * query:String(required)
  * timeRange:String(optional)
  * aggregateField:String(optional)
  * orderColumn:String(optional)
  * orderType:String(optional)
  * filters:String(optional)
  * page:Integer(optional,default=0)
  * pageSize:Integer(optional,default=30)
* response parameters:
  * collection:String
  * query:String
  * timeRange:String
  * aggregateField:String
  * orderColumn:String
  * orderType:String
  * filters:String
  * page:Integer
  * pageSize:Integer
* request example:

```
curl -i 'http://10.16.1.59:10020/solr/search/request?collection=search&query=name:long&timeRange="thisyear"&aggregateField=timestamp&pageSize=3'

```
```
{
  "code": "200000",
  "result": {
    "params": {
      "aggregateField": "timestamp",
      "ignoreCase": true,
      "query": "*:*",
      "pageSize": 30,
      "collection": "search",
      "filters": null,
      "page": 0,
      "fields": null,
      "direction": "asc",
      "order": "id",
      "timeRange": "thisyear"
    },
    "supplies": {
      "total": 6
    },
    "items": [
      {
        "marked": 1,
        "score": null,
        "sourceGroups": "group1",
        "_version_": 1578868741268045800,
        "createId": 10011001,
        "name": "long",
        "anonymous": 1,
        "id": 10010,
        "domainId": "101010",
        "timestamp": 1486059721320
      },
      {
        "score": null,
        "_version_": 1579506525029793800,
        "name": "hellsdsdfalsdfasdfasdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdfas;dfja;sdfj;asdf;sddfla;sdf;asd;fja;sdf;klsdfasdhasdjf;asddfjasdjfjasfa;sjdf;a;sdf;sdkfa'",
        "id": 10020,
        "timestamp": 1487892142232
      },
      {
        "marked": 1,
        "score": null,
        "sourceGroups": "group4",
        "_version_": 1578935153399431200,
        "createId": 10011001,
        "name": "long",
        "anonymous": 1,
        "id": 10323,
        "domainId": "101010",
        "timestamp": 1496427721320
      },
      {
        "marked": 1,
        "score": null,
        "sourceGroups": "group3",
        "_version_": 1578935125648867300,
        "createId": 10011001,
        "name": "long",
        "anonymous": 1,
        "id": 100120,
        "domainId": "101010",
        "timestamp": 1493749321320
      },
      {
        "marked": 1,
        "score": null,
        "sourceGroups": "group4",
        "_version_": 1578935137433813000,
        "createId": 10011001,
        "name": "long",
        "anonymous": 1,
        "id": 100123,
        "domainId": "101010",
        "timestamp": 1501698121320
      },
      {
        "marked": 1,
        "score": null,
        "sourceGroups": "group3",
        "_version_": 1578935112917057500,
        "createId": 10011001,
        "name": "long",
        "anonymous": 1,
        "id": 1001110,
        "domainId": "101010",
        "timestamp": 1486059721320
      }
    ]
  }
}
```
### All Collections
* description：
  Get All collections in cluster
* path:/solr/search/collection
* method:GET
* request parameters:none
* response parameters:
  * items:List\<String\>
  * numFound:int
* request example:

```
curl http://10.16.1.59:10020/solr/search/collection
```
* response example:

```
{
  "code": "200000",
  "result": {
    "numFound": 1,
    "items": [
      "search"
    ]
  }
}
```

### Fields of Collection
* description:
  Get fields of specified collection
* path:/solr/search/collection/field
* method:GET
* request parameters:
  * collection:String(not null)
  * asc:Boolean
* response parameters:
  * collectionName:String
  * items:List\<String\>
  * asc:Boolean
* request example:

```
curl 'http://10.16.1.59:10020/solr/search/collection/field?collection=collection1&asc=false'
```
* response example:

```
{
  "code": "200000",
  "result": {
    "collectionName": "search",
    "numFound": 11,
    "asc": true,
    "items": [
      "_version_",
      "anonymous",
      "createId",
      "domainId",
      "id",
      "marked",
      "name",
      "sourceGroups",
      "testfield",
      "text",
      "timestamp"
    ]
  }
}
```

### Details of Field
* description:
  Get index information of specified field which satisfy limits
* path:/solr/search/collection/field/detail
* method:GET
* request parameters:
  * collection:String(required)
  * field:String(required)
  * count:int(optional,default=1)
  * num:int(optional,default=-1)
  * asc:Boolean(optional,default=true)
* response parameters:
  * collectionName:String
  * fieldName:String
  * count:int
  * num:int
  * numFound:int
  * asc:Boolean
  * items:Map<String,int>
* request example:

```
curl 'http://10.16.1.59:10020/solr/search/collection/field/detail?collection=collection1&field=field1&count=0&num=-1&asc=false'
```
* response example:

```
{
  "code": "200000",
  "result": {
    "collectionName": "search",
    "fieldName": "id",
    "numFound": 7,
    "count": 1,
    "num": -1,
    "asc": true,
    "items": {
      "10010": 1,
      "10020": 1,
      "10323": 1,
      "100120": 1,
      "100123": 1,
      "1001023": 1,
      "1001110": 1
    }
  }
}
```
### Aggregation by GROUP 
* description:<br>
get the statistics information for current query
* path:<br>
/solr/search/aggregate/group
* request parameters:<br>
  * collection:String(required)
  * aggregateField:String(required)
  * timeRange:String(required)
  * query:String(optional)
  * filters:String(optional)
  * type:Enum("GROUP")(required)
  * outerField:String(required)
  * outerFieldCategory:Enum("DATE","NUMBER","TEXT")(required)
  * limit:int(required,-1 means return all items)
  * outerStart:String(required)
  * outerEnd:String(required)
  * outerGap:String(required)
  * tagField:String(required)
* request example:<br>

```
curl 'http://localhost:8080/solr/search/aggregate/group?collection=log-production&aggregateField=log_dt&timeRange=lastyear&type=GROUP&outerField=costTimeA&limit=3&outerFieldCategory=NUMBER&outerStart=0&outerEnd=10&outerGap=4'
```
```
{
  "code": "200000",
  "result": {
    "params": {
      "query": null,
      "outerGap": "4",
      "collection": "log-production",
      "filters": null,
      "outerStart": "0",
      "type": "GROUP",
      "outerFieldCategory": "NUMBER",
      "outerField": "costTimeA",
      "outerEnd": "10",
      "tagField": null,
      "granularity": null,
      "aggregateFiled": "log_dt",
      "limit": 3,
      "timeRange": "lastyear"
    },
    "supplies": null,
    "items": [
      {
        "name": "0",
        "count": 170322,
        "countList": null
      },
      {
        "name": "4",
        "count": 37579,
        "countList": null
      },
      {
        "name": "8",
        "count": 29796,
        "countList": null
      },
      {
        "name": "12",
        "count": -1,
        "countList": null
      }
    ]
  }
}
```
### Aggregation by VALUE  
* description:<br>
* path:<br>
/solr/search/aggregate/value
* request parameters:<br>
  * collection:String(required)
  * aggregateField:String(required)
  * timeRange:String(required)
  * query:String(optional)
  * filters:String(optional)
  * type:Enum("VALUE")(required)
  * outerField:String(required)
  * outerFieldCategory:Enum("DATE","NUMBER","TEXT")(required)
  * limit:int(required,-1 means return all items)
* request example:<br>

```
curl 'http://localhost:8080/solr/search/aggregate/value?collection=log-production&aggregateField=log_dt&timeRange=lastyear&type=VALUE&outerField=IP&limit=3&outerFieldCategory=TEXT'
```
```
{
  "code": "200000",
  "result": {
    "params": {
      "query": null,
      "outerGap": null,
      "collection": "log-production",
      "filters": null,
      "outerStart": null,
      "type": "VALUE",
      "outerFieldCategory": "TEXT",
      "outerField": "IP",
      "outerEnd": null,
      "tagField": null,
      "granularity": null,
      "aggregateFiled": "log_dt",
      "limit": 3,
      "timeRange": "lastyear"
    },
    "supplies": null,
    "items": [
      {
        "name": "10.0.30.162",
        "count": 7933,
        "countList": null
      },
      {
        "name": "10.0.30.20",
        "count": 7720,
        "countList": null
      },
      {
        "name": "117.88.130.69",
        "count": 6114,
        "countList": null
      }
    ]
  }
}
```
### Aggregation by VALUE_GROUP
* description:<br>
get the statistics information for current query
* path:<br>
/solr/search/aggregate/valuegroup
* request parameters:<br>
  * collection:String(required)
  * aggregateField:String(required)
  * timeRange:String(required)
  * query:String(optional)
  * filters:String(optional)
  * type:Enum("GROUP")(required)
  * outerField:String(required)
  * outerFieldCategory:Enum("DATE","NUMBER","TEXT")(required)
  * innerField:String(required)
  * innerFieldCategory:Enum("VALUE","GROUP","GROUP_VALUE")
  * limit:int(required, -1 means return all items)
  * innerStart:String(required)
  * innerEnd:String(required)
  * innerGap:String(required)
* request example:<br>

```
curl 'http://localhost:8080/solr/search/aggregate/valuegroup?collection=log-production&aggregateField=log_dt&timeRange=lastyear&type=VALUE_GROUP&outerField=IP&limit=3&outerFieldCategory=TEXT&innerField=log_dt&innerFieldCategory=DATE&innerStart=2016-05-00 00:00:00&innerEnd=2016-06-00 00:00:00&innerGap=15 day'
```
```  
{
  "code": "200000",
  "result": {
    "params": {
      "innerGap": "15 day",
      "innerStart": "2016-05-00 00:00:00",
      "innerEnd": "2016-06-00 00:00:00",
      "query": null,
      "outerGap": null,
      "collection": "log-production",
      "filters": null,
      "outerStart": null,
      "type": "VALUE_GROUP",
      "outerFieldCategory": "TEXT",
      "outerField": "IP",
      "outerEnd": null,
      "tagField": null,
      "innerField": "log_dt",
      "granularity": null,
      "innerFieldCategory": "DATE",
      "aggregateFiled": "log_dt",
      "limit": 3,
      "timeRange": "lastyear"
    },
    "supplies": null,
    "items": [
      {
        "name": "10.0.30.162",
        "count": null,
        "countList": [
          {
            "name": "1461945600001",
            "count": 0,
            "countList": null
          },
          {
            "name": "1463241600001",
            "count": 0,
            "countList": null
          },
          {
            "name": "1464537600001",
            "count": 7933,
            "countList": null
          },
          {
            "name": "1465862400001",
            "count": -1,
            "countList": null
          }
        ]
      },
      {
        "name": "10.0.30.20",
        "count": null,
        "countList": [
          {
            "name": "1461945600001",
            "count": 0,
            "countList": null
          },
          {
            "name": "1463241600001",
            "count": 0,
            "countList": null
          },
          {
            "name": "1464537600001",
            "count": 7720,
            "countList": null
          },
          {
            "name": "1465862400001",
            "count": -1,
            "countList": null
          }
        ]
      },
      {
        "name": "117.88.130.69",
        "count": null,
        "countList": [
          {
            "name": "1461945600001",
            "count": 0,
            "countList": null
          },
          {
            "name": "1463241600001",
            "count": 0,
            "countList": null
          },
          {
            "name": "1464537600001",
            "count": 6114,
            "countList": null
          },
          {
            "name": "1465862400001",
            "count": -1,
            "countList": null
          }
        ]
      }
    ]
  }
}
```
### Aggregation by DATE
* description:<br>
get the aggregation result of a query, which is special case of range facet with a 'DATE' field
* path:<br>
/solr/search/aggregate/date
* request parameters:<br>
  * collection:String(required)
  * aggregateField:String(required)
  * timeRange:String(required)
  * query:String(optional)
  * filters:String(optional)
  * granularity:String(optional)
  * type:ENUM("DATE")(optional,default:DATE)
* response example:<br>
  * success scenario:

  ```
  curl 'http://localhost:8080/solr/search/aggregate/date?collection=log-production&aggregateField=log_dt&timeRange=lastyear'
  ```
  ```
  {
    "code": "200000",
    "result": {
      "params": {
        "granularity": null,
        "aggregateFiled": "log_dt",
        "query": null,
        "collection": "log-production",
        "filters": null,
        "type": null,
        "timeRange": "lastyear"
      },
      "supplies": null,
      "items": [
        {
          "name": "1451491200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1454083200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1456675200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1459267200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1461859200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1464451200001",
          "count": 530834,
          "countList": null
        },
        {
          "name": "1467043200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1469635200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1472227200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1474819200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1477411200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1480003200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1482595200001",
          "count": 0,
          "countList": null
        },
        {
          "name": "1485216000001",
          "count": -1,
          "countList": null
        }
      ]
    }
  }
  ```
  * failure scenario:

  ```
  curl 'http://localhost:8080/solr/search/aggregate/date?collection=collection-missed&aggregateField=log_dt&timeRange=lastyear'
  ```
  ```
  {
    "code": "200002",
    "params": {
      "granularity": null,
      "aggregateFiled": "log_dt",
      "query": null,
      "collection": "collection-missed",
      "filters": null,
      "type": null,
      "timeRange": "lastyear"
    },
    "message": "Collection not found: collection-missed"
  }
  ```

### Get Aggregation Fields
* description:<br>
Get the fields list for aggregation
* URL:/solr/search/collection/field/aggregate
* method:Get
* request parameter:
  * collection:String(required)
* response parameter:
  * code:String
  * result:List
* response example:

```
  {
    "code": "200000",
    "result": [
      "log_dt"
    ]
  }
```
  
### Get Categorized Fields
* description:<br>
 classify the fields into different categories. 
* URL:/solr/search/collection/field/categorized
* method:GET
* request parameter:
  * collection:String(required) 
* response parameter:
  name:String
  category:Enum("TEXT","DATE","NUMBER")
* response example:

```
  {
   "code": "200000",
   "result": {
     "params": {
       "collection": "log-production"
     },
     "supplies": null,
     "items": [
       {
         "name": "log_dt",
         "category": "DATE"
       },
       {
         "name": "IP",
         "category": "TEXT"
       },
       {
         "name": "MAC",
         "category": "TEXT"
       },
       {
         "name": "_id",
         "category": "NUMBER"
       }
     ]
   }
  } 
```

### Utility Service
* description:<br>
get a gap of date range
* path:/solr/search/utility/gap
* method:GET
* parameter:
  * start:String(required)
  * end:String(required)
* request example:

```
curl 'http://localhost:8080/solr/search/utility/gap?start=2017-12-23 23:32:23&end=2017-12-30 23:32:23'
```
* response example:

```
{
  "code": "200000",
  "result": {
    "params": {
      "start": "2017-12-23 23:32:23",
      "end": "2017-12-30 23:32:23"
    },
    "supplies": null,
    "items": [
      "17 hour"
    ]
  }
}
```
