## RxFile 

RxFile 帮助应用初始化时，自动生成默认文件夹。带有创建文件和删除文件功能


## 默认生成的文件夹

```java
  /**
   * 默认存放根目录
   */
  static final String ROOT = "rxfile";
  
  /**
   * 保存日志存放位置
   */
  static final String CRASH = "crash";
  
  /**
   * 存放网络请求
   */
  static final String NETWORK_CACHE = "network_cache";
  
  /**
   * 屏幕截图
   */
  static final String GALLERY = "gallery";
  
  /**
   * 网络图片存放
   */
  static final String IMAGE = "image";
  
  /**
   * 网络图片缓存
   */
  static final String IMAGE_CACHE = "image_cache";
  
  /**
   * apk 下载存放
   */
  static final String APK = "apk";
```


## 初始化

在 Application 类中，初始化 RxFile 

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxFile.init(this);
    }
}
```

如果，需要自定义应用初始化文件夹

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxFile.init(this, new FileEntity.Build()
                .isAddDefaultDir(true)  // 是否添加默认文件
                .setExternalRootDir("root123") // 自定义添加外部存储 Root 跟目录
                .addExternalDir("dir1") // 自定义添加外部存储文件
                .addInternalDir("inter1") // 自定义添加内部存储文件
                .build());
    }
}
```



## 创建文件夹

```java
RxFile.makeFile("dir1"); // 在当前跟目录下，创建文件

RxFile.apkFile(); // 获取默认apk文件

RxFile.deleteFile("dir1"); // 删除根目录下文件
```

