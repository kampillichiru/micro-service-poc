We are to get the response like below using below mentioned KIBANA devtool and postman.
We want to achive using programaticallly in springboot application.

Need step by step code and should get exact results

elastic search Url: https://elastic-search/myapp/_search

restquest Body:

{
  "fields": ["status_code", "name", {"field":"@timestamp", "format": "strict_date_optional_time"}],
  "query": {
    "bool": {
      "must": [],
      "filter": [
        {"range": {"status_code": {"gte": 100, "lte": 600}}},
        {
          "bool": {
            "should": [
              {"match_phrase": {"name": "MY_RESPONSE"}},
              {"match_phrase": {"name": "My Request_REQUEST"}}
            ],
            "minimum_should_match": 1
          }
        },
        {"range": {"@timestamp": {"format": "strict_date_optional_time", "gte": "2023-03-25T00:29:08.691Z"}}}
      ]
    }
  },
  "aggs": {
    "api_name": {
      "terms": {
        "field": "name.keyword",
        "aggs": {
          "status_ranges": {
            "range": {
              "field": "status_code",
              "ranges": [
                {"from": 200, "to": 299, "key": "SUCCESS"},
                {"from": 400, "to": 499, "key": "CS_ERROR"},
                {"from": 500, "to": 599, "key": "SS_ERROR"}
              ]
            }
          }
        }
      }
    }
  }
}



Response body:

{
  "aggregations": {
    "api_name": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "FEIGN_RESPONSE",
          "doc_count": 657618,
          "status_ranges": {
            "buckets": [
              {
                "key": "SUCCESS",
                "from": 200.0,
                "to": 299.0,
                "doc_count": 638009
              },
              {
                "key": "CS_ERROR",
                "from": 400.0,
                "to": 499.0,
                "doc_count": 19222
              },
              {
                "key": "SS_ERROR",
                "from": 500.0,
                "to": 599,
                "doc_count": 387
              }
            ]
          }
        }
      ]
    }
  }
}




{
  "fields": [
    "status_code",
    "name",
    {
      "field": "@timestamp",
      "format": "strict_date_optional_time"
    }
  ],
  "query": {
    "bool": {
      "must": [],
      "filter": [
        {
          "range": {
            "status_code": {
              "gte": 100,
              "lte": 600
            }
          }
        },
        {
          "bool": {
            "should": [
              {
                "match_phrase": {
                  "name": "MY_RESPONSE"
                }
              },
              {
                "match_phrase": {
                  "name": "My Request_REQUEST"
                }
              }
            ],
            "minimum_should_match": 1
          }
        },
        {
          "range": {
            "@timestamp": {
              "format": "strict_date_optional_time",
              "gte": "2023-03-25T00:29:08.691Z",
              "lte": "2023-05-25T00:29:08.691Z"
            }
          }
        }
      ]
    }
  },
  "aggs": {
    "status_ranges": {
      "range": {
        "field": "status_code",
        "ranges": [
          {
            "from": 200,
            "to": 299,
            "key": "SUCCESS"
          },
          {
            "from": 400,
            "to": 499,
            "key": "CS_ERROR"
          },
          {
            "from": 500,
            "to": 599,
            "key": "SS_ERROR"
          }
        ]
      },
      "aggs": {
        "latest_timestamp": {
          "max": {
            "field": "@timestamp"
          }
        }
      }
    }
  }
}
