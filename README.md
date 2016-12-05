WoYun - 蜗云
============

状态说明
--------

| status | 说明 |
|--------|------|
| -1     | 失败 |
| 0      | 成功 |

WEB 接口说明
------------

### 获取仓库信息

#### 请求

`GET http://woyun.cn/wesign/repos`

#### 响应

```
{
	"status": 0,
	"desc": "操作成功",
	"data": 
		[
			"id": 4,
			"repoId": "df0786aa-a21f-4c6b-b7f3-96cd2596d8e3",
			"repoName": "我的资料库",
			"userEmail": "japinli@qq.com",
			"createTime": 1480599651000,
			"modifyTime": 1480599650000,
			"state": false
		]
}
```

### 新建仓库

#### 请求

`POST http://woyun.cn/wesign/repos`

#### 参数

```
{
	"repoName": "我的文档"
}
```

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 仓库重命名

#### 请求

`PUT http://woyun.cn/wesign/repos`

#### 参数

```
{
	"repoId": "df0786aa-a21f-4c6b-b7f3-96cd2596d8e3",
	"repoName": "我的文档"
}
```

**备注:** *repoName* 为更新的仓库名称。

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 删除仓库

#### 请求

`DELETE http://woyun.cn/wesign/repos`

#### 参数

```
{
	"repoId": "df0786aa-a21f-4c6b-b7f3-96cd2596d8e3",
	"repoName": "我的文档"
}
```

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 获取目录信息

#### 请求

`GET http://woyun.cn/wesign/repos/{repoid}/dir?path=directory`

#### 参数

* repoid - 仓库ID编号
* path - 仓库下的目录路径

#### 响应

```
{
	"status": 0,
	"desc": "操作成功",
	"data":
		[
			"type": "dir",
			"filename": "文档",
			"size": 10240,
			"mtime": 1480599650000
		]
}
```

**备注:** 

1. `type` 字段类型分为: `dir` 和 `file` 两类。
2. `size` 字段的单位为 *byte* 。
