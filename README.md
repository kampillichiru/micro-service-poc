{
  "size": 0,
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "@timestamp": {
              "gte": "2023-03-25T00:29:08.6917",
              "lte": "2024-04-01T06:44:08.6912",
              "format": "strict_date_optional_time"
            }
          }
        },
        {
          "terms": {
            "name.keyword": ["MYAPI-1", "MYAPI-2", "MYAPI-3"]
          }
        },
        {
          "range": {
            "status_code": {
              "gte": 200,
              "lte": 600
            }
          }
        }
      ]
    }
  },
  "aggs": {
    "api_name": {
      "terms": {
        "field": "name.keyword",
        "size": 10
      },
      "aggs": {
        "status_ranges": {
          "range": {
            "field": "status_code",
            "ranges": [
              {"from": 200, "to": 299, "key": "success"},
              {"from": 400, "to": 499, "key": "client side error"},
              {"from": 500, "to": 599, "key": "server side error"}
            ]
          }
        }
      }
    }
  }
}

