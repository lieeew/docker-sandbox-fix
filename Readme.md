# 数据库

新增 `code_question` 表

```mysql
create table code_question
(
    id            bigint auto_increment
        primary key,
    question_id   bigint                                 not null comment 'question_Id',
    title         varchar(255)                           not null,
    description   text                                   null,
    class_name    varchar(128) default 'Solution'        null,
    method_name   varchar(128)                           not null,
    param_types   text                                   not null comment '类型参数 ["int[]", "int"]',
    return_type   varchar(128)                           not null comment '返回值类型 	"int" ',
    code_template text                                   not null,
    test_cases    json                                   not null comment '比如 [ { "params": [[1, 2, 3, 4], 3], "expected": 2 } ]',
    create_time   datetime     default CURRENT_TIMESTAMP null,
    updateTime    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint      default 0                 not null comment '是否删除'
);

create index idx_question_id
    on code_question (question_id);


```

demo 测试数据

```sql
INSERT INTO yuoj.code_question (title, description, class_name, method_name, param_types, return_type, code_template, test_cases, create_time, update_time, is_delete) VALUES (1, 1898761487686696962, '二分查找实现', '实现一个简单的二分查找函数，返回目标值在数组中的索引，找不到返回 -1。', 'Solution', 'search', '["int[]", "int"]', 'int', 'public class Solution {
    public int search(int[] nums, int target) {
        // 二分查找逻辑
    }
}', '[{"params": [[1, 2, 3, 4], 3], "expected": 2}, {"params": [[1, 2, 3, 4], 5], "expected": -1}]', '2025-05-20 18:11:54', '2025-05-20 18:15:48', 0);

```

# 接口测试

```json
{
  "inputList": [],
  "code": "public class Solution {\n    public int search(int[] nums, int target) {\n        int left = 0, right = nums.length;\n        while (left < right) {\n            int mid = left + (right - left) / 2;\n            if (nums[mid] == target) {\n                return mid;\n            } else if (nums[mid] > target) {\n                right = mid;\n            } else {\n                left = mid + 1;\n            }\n        }\n        if (left <= 0 || left >= nums.length) {\n            return -1;\n        }\n        return nums[left] == target ? left : -1;\n    }\n}",
  "language": "java",
  "codeQuestion": {
    "id": 1,
    "questionId": 101,
    "title": "两数之和",
    "description": "给定一个整数数组和一个目标值，找出数组中和为目标值的两个数的下标。",
    "className": "Solution",
    "methodName": "search",
    "paramTypes": "[\"int[]\", \"int\"]",
    "returnType": "int[]",
    "codeTemplate": "public int[] twoSum(int[] nums, int target) {\n    // TODO: 请实现\n    return new int[]{};\n}",
    "testCases": [
      {
        "params": [[1, 2, 3, 4], 3],
        "expected": ["9"]
      }
    ],
    "createTime": "2024-05-22T12:00:00",
    "updatetime": "2024-05-22T13:00:00",
    "isdelete": 0
  }
}

```

```json
{
  "inputList": [],
  "code": " import java.util.*; public class Solution {\n    public List<List<String>> groupAnagrams(String[] strs) {\n        Map<String, List<String>> map = new HashMap<String, List<String>>();\n        for (String str : strs) {\n            char[] array = str.toCharArray();\n            Arrays.sort(array);\n            String key = new String(array);\n            List<String> list = map.getOrDefault(key, new ArrayList<String>());\n            list.add(str);\n            map.put(key, list);\n        }\n        return new ArrayList<List<String>>(map.values());\n    }\n}\n",
  "language": "java",
  "codeQuestion": {
    "id": 2,
    "questionId": 102,
    "title": "字母异位词分组",
    "description": "给定一个字符串数组，将字母异位词组合在一起。",
    "className": "Solution",
    "methodName": "groupAnagrams",
    "paramTypes": "[\"String[]\"]",
    "returnType": "List<List<String>>",
    "codeTemplate": "public List<List<String>> groupAnagrams(String[] strs) {\n    Map<String, List<String>> map = new HashMap<>();\n    for (String str : strs) {\n        char[] array = str.toCharArray();\n        Arrays.sort(array);\n        String key = new String(array);\n        List<String> list = map.getOrDefault(key, new ArrayList<>());\n        list.add(str);\n        map.put(key, list);\n    }\n    return new ArrayList<>(map.values());\n}",
    "testCases": [
      {
        "params": [
          [
            "eat",
            "tea",
            "tan",
            "ate",
            "nat",
            "bat"
          ]
        ],
        "expected": [
          [
            "eat",
            "tea",
            "ate"
          ],
          [
            "tan",
            "nat"
          ],
          [
            "bat"
          ]
        ]
      },
      {
        "params": [
          [
            ""
          ]
        ],
        "expected": [
          [
            ""
          ]
        ]
      },
      {
        "params": [
          [
            "a"
          ]
        ],
        "expected": [
          [
            "a"
          ]
        ]
      }
    ]
  }
}
```