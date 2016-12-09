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

### 检测仓库是否存在

#### 请求

`GET http://woyun.cn/wesign/repos/check?repoName=Documents`

#### 参数

- repoName - 待检测的用户仓库名

#### 响应

```
{
	"status": 0,
	"desc": "操作成功",
	"data": true
}
```

**备注:**

| data | 说明           |
|------|----------------|
| true | 该仓库已经存在 |
| false| 该仓库不存在   |

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

### 获取仓库变更记录

#### 请求

`GET http://woyun.cn/wesign/repos/{repo-id}/history`

### 响应

```
{
	"status": 0,
	"desc": "操作成功",
	"data":
		[
			{
				"commitId": "f825b327d3020a1e06cef5f737f899373ddd3039",
				"committer": "japinli",
				"email": "japinli@qq.com",
				"commitTime": 1481080888,
				"message": "获取仓库记录测试"
			},
			{
				"commitId": "79c5d4e2bfd2db1227e9d3d31271ae4c9bac9a77",
				"committer": "japinli",
				"email": "japinli@qq.com",
				"commitTime": 1480980123,
				"message": "新建仓库"
			}
		]
}
```

### 获取目录信息

#### 请求

`GET http://woyun.cn/wesign/repos/{repo-id}/dir?path=directory`

#### 参数

* repo-id - 仓库ID编号
* path - 仓库下的目录路径

**备注:**

若是获取仓库下的根目录，则将 *path* 置空。

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

`POST http://woyun.cn/wesign/repos/{repo-id}/dir`

#### 参数

 - repo-id - 仓库编号

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

`PUT http://woyun.cn/wesign/repos/{repo-id}/dir`

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

 - repo-id - 仓库id编号
 
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

### 文件上传

#### 请求

`POST http://woyun.cn/wesign/repos/{repo-id}/file`

**备注:**

该接口需要依赖前端通过表单提交。

#### 参数

 - repo-id - 仓库id编号
 - files - 待上传的文件
 - path - 上传路径 (仓库中的路径)
 
#### 响应

```
{
	"status": 0,
	"desc": "操作成功"
}
```

### 文件下载

#### 请求

`GET http://woyun.cn/wesign/repos/{repo-id}/file?path=directory&name=filename1&name=filename2`

#### 参数

 - repo-id - 仓库id编号
 - path - 在仓库中的路径
 - name - 待下载的文件，可包含多个
 
### 历史文件下载

#### 请求

`GET http://woyun.cn/wesign/repos/{repo-id}/history/file?commitId=xxxx&path=xxxxx&name=xxx&name=xxx`

#### 参数

 - repo-id - 仓库id编号
 - commitId - 提交记录编号 (SHA-1)
 - path - 在仓库中的路径
 - name - 待下载的文件，可包含多个

### 分类获取文件信息

#### 请求

`GET http://woyun.cn/wesign/repos/category?category=doc`

#### 参数

 - category - 类别: `doc`, `video`, `image` 以及 `others`
 
#### 响应

```
{
	"status": 0,
	"desc": "操作成功",
	"data":
		[
			{
				"repoId": "df0786aa-a21f-4c6b-b7f3-96cd2596d8e3",
				"repoName": "我的资料库",
				"fileInfo":
					[
						{
							"type": "file",
							"filename": "README.md",
							"size": 113,
							"mtime": 1481198034
						},
						{
							"type": "file",
							"filename": "NOTE.txt",
							"size": 514,
							"mtime": 1481198073
						}
					]
			},
			{
				"repoId": "ef4586a3-432a-4c6b-b7f3-96cd2596d8e3",
				"repoName": "测试库",
				"fileInfo":
					[
						{
							"type": "file",
							"filename": "测试.doc",
							"size": 20480,
							"mtime": 1481197918
						},
						{
							"type": "file",
							"filename": "入门手册.pdf",
							"size": 31450,
							"mtime": 1481807218
						}
					]
			}
		]
}
```
