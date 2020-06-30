# kotlin-retrofit
用kotlin语言编写的retrofit网络请求封装库，
支持get post put delete download upload 。
支持表单(postForm、putForm)请求。
支持文件上传、下载支持进度回调。
支持在Activity销毁时，自动取消网络。

### 引入

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.wenkency:kotlin-retrofit:1.1.0'
implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
implementation 'androidx.core:core-ktx:1.3.0'
implementation 'androidx.appcompat:appcompat:1.1.0'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:adapter-rxjava3:2.9.0'
implementation 'com.google.code.gson:gson:2.8.6'
implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
implementation 'io.reactivex.rxjava3:rxjava:3.0.0

```
### Application初始化
```
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 1. 初始化
        RestConfig.getInstance()
                .baseUrl("http://httpbin.org/")
                .debugUrl("http://httpbin.org/")
                .debug(true)
                .setCommHeaders(null) // 添加公共请求头，根据项目自己添加
                .setCommParams(null)// 添加公共请求参数，根据项目自己添加
                .addInterceptor(interceptor)
                // 可设置10内再次请求，走缓存
                // .addNetInterceptor(new CacheInterceptor(10))
                .register(this);
    }
}
```

### 使用方式
```
private fun requestNet() {
    RxRetrofitPresenter.post(activity, "post", Bean("100"),
        object : BeanCallback<String>() {

            override fun onError(code: Int, message: String) {
                println(message)
            }

            override fun onSucceed(result: String) {
                btn.text = result
            }
        })
}
```