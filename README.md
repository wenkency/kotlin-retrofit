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

implementation 'com.github.wenkency:kotlin-retrofit:2.0.0'
implementation ('com.squareup.retrofit2:retrofit:2.8.0'){
    exclude group: 'com.android.support'
    exclude group: 'com.squareup.okio'
    exclude group: 'com.squareup.okhttp3'
    exclude group: 'com.squareup.okhttp'
}
implementation "com.squareup.okhttp3:okhttp:3.12.1"
implementation "com.squareup.okio:okio:2.6.0"
implementation 'com.google.code.gson:gson:2.8.6'
// RXJava
implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
implementation 'io.reactivex.rxjava3:rxjava:3.0.0'
// 反射
implementation  "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"

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
/**
 * post请求
 */
private fun post() {
    RetrofitPresenter.post(activity, "post", Bean("100"),
        object : BeanCallback<String>() {

            override fun onError(code: Int, message: String) {
                println(message)
            }

            override fun onSucceed(result: String) {
                btn.text = result
            }
        })
}

/**
 * 同时请求两个接口
 */
private fun async() {
    val service = RestCreator.getService()
    GlobalScope.launch(Dispatchers.Main) {
        Log.e("TAG", "async---1---")
        val getResult = async(Dispatchers.IO) {
            val get = service.get("get", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
            val response = get.execute()
            Log.e("TAG", "async---2---")
            response.body()!!.string()
        }
        val postResult = async(Dispatchers.IO) {
            val get =
                service.postForm("post", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
            val response = get.execute()
            Log.e("TAG", "async---3---")
            response.body()!!.string()
        }
        Log.e("TAG", "async---4---")

        btn.text = getResult.await() + "\n" + postResult.await()

        Log.e("TAG", "async---5---")
    }
    Log.e("TAG", "async---6---")
    // 6 1 4 3 2 5
    // 6 1 4 2 3 5
}

```