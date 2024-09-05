```
PUT /goods
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "goodsId":{
        "type": "keyword"
      },
      "goodsName":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "goodsPrice":{
        "type": "double"
      },
      "goodsBrand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "goodsType":{
        "type": "keyword",
        "copy_to": "all"
      },
      "goodsConnectType":{
        "type": "keyword",
        "copy_to": "all"
      },
      "goodsUrl":{
      	"type":"keyword"
      },
      "goodsIntroduction":{
      	"type":"keyword"
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion", 
          "analyzer": "completion_analyzer"
      }
    }
  }
}
```

```
PUT /article
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "articleId":{
        "type": "keyword"
      },
      "userId":{
        "type": "keyword"
      },
      "articleTitle":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "articleImgUrl":{
      	"type":"keyword"
      },
      "articleContextUrl":{
      	"type":"keyword"
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion", 
          "analyzer": "completion_analyzer"
      }
    }
  }
}
```

