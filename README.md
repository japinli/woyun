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
			{
				"id": 4,
				"repoId": "df0786aa-a21f-4c6b-b7f3-96cd2596d8e3",
				"repoName": "我的资料库",
				"userEmail": "japinli@qq.com",
				"createTime": 1480599651000,
				"modifyTime": 1480599650000,
				"state": false
			},
			{
				"id": 5,
				"repoId": "ef4586a3-432a-4c6b-b7f3-96cd2596d8e3",
				"repoName": "测试库",
				"userEmail": "japinli@qq.com",
				"createTime": 1480599651000,
				"modifyTime": 1480599650000,
				"state": false
			}
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
			{
				"type": "dir",
				"filename": "文档",
				"size": 10240,
				"mtime": 1480599650000
			},
			{
				"type": "file",
				"filename": "README.md",
				"size": 456,
				"mtime": 1480599650000
			}
		]
}
```

**备注:** 

1. `type` 字段类型分为: `dir` 和 `file` 两类。
2. `size` 字段的单位为 *byte* 。

### 新建目录

#### 请求

`POST http://woyun.cn/wesign/repos/{repoid}/dir`

#### 参数

```
{
	"path": "我的文档",
	"name": "文档"	
}
```

**说明:**

上述请求说明在 *我的文档* 目录下新建 *文档* 目录。

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 目录重命名

#### 请求

`PUT http://woyun.cn/wesign/repos/{repoid}/dir`

#### 参数

```
{
	"path": "我的文档",
	"name": "文档",
	"newName": "Documents"
}

```

**说明:**

上述请求说明将 *我的文档* 目录下的 *文档* 目录重命名为 *Documents* 目录。

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 复制文件(夹)

#### 请求

`POST http://woyun.cn/wesign/repos/operation`

#### 参数

```
{
	"operation": "copy",
	"srcRepoId": "我的资料库",
	"srcPath": "文档",
	"name": "README.md",
	"dstRepoId": "测试库",
	"srcPath": "/"
}
```

**说明:**

上述请求说明将 *我的资料库* 中 *文档* 目录下的 *README.md* 文件复制到 *测试库* 的根目录下。


#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 移动文件(夹)

#### 请求

`POST http://woyun.cn/wesign/repos/operation`

#### 参数

```
{
	"operation": "move",
	"srcRepoId": "我的资料库",
	"srcPath": "文档",
	"name": "README.md",
	"dstRepoId": "测试库",
	"srcPath": "/"
}
```

**说明:**

上述请求说明将 *我的资料库* 中 *文档* 目录下的 *README.md* 文件移动到 *测试库* 的根目录下。

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 删除文件(夹)

#### 请求

`DELETE http://woyun.cn/wesign/repos/{repo-id}`

#### 参数

```
[
	{
		"path": "我的文档/README.md"
	},
	{
		"path": "我的文档/INSTALL.md"
	}
]
```

#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```
