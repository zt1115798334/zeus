#接口帮助
###startDate endDate 开始时间结束时间需要自己修改
#### 采集词获取文章到接口
curl -X POST "http://localhost:8899/api/pull/pullArticleOfGatherWords" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=INTERFACE"
#### 采集词获取文章到本地
curl -X POST "http://localhost:8899/api/pull/pullArticleOfGatherWords" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=LOCAL"
#### 自定义词获取文章到接口
curl -X POST "http://localhost:8899/api/pull/pullArticleOfCustomWords" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=INTERFACE"
#### 自定义词获取文章到本地
curl -X POST "http://localhost:8899/api/pull/pullArticleOfCustomWords" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=LOCAL"
#### 采集作者获取文章到接口
curl -X POST "http://localhost:8899/api/pull/pullArticleOfGatherAuthors" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=INTERFACE"
#### 采集作者获取文章到本地
curl -X POST "http://localhost:8899/api/pull/pullArticleOfGatherAuthors" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=LOCAL"
#### 自定义作者获取文章到接口
curl -X POST "http://localhost:8899/api/pull/pullArticleOfCustomAuthors" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=INTERFACE"
#### 自定义作者获取文章到本地
curl -X POST "http://localhost:8899/api/pull/pullArticleOfCustomAuthors" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "startDate=2021-08-08" -F "status=true" -F "storageMode=LOCAL"
#### 搜索词获取文章到接口
curl -X POST "http://localhost:8899/api/pull/pullArticleOfQueryWords" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "queryWords=搜搜" -F "startDate=2021-08-08" -F "storageMode=INTERFACE"
#### 搜索词获取文章到本地
curl -X POST "http://localhost:8899/api/pull/pullArticleOfQueryWords" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "endDate=2021-08-08" -F "queryWords=搜搜" -F "startDate=2021-08-08" -F "storageMode=LOCAL"
#### 设置日志等级
curl -X POST 'localhost:8899/actuator/loggers/com.zt.zeus.transfer.utils.HttpClientUtils' -H 'Content-Type: application/json' -H 'Cookie: JSESSIONID=E172150E097D6ADCD9ABB813BE25FFA2' --data-raw '{
"configuredLevel": "INFO"
}'