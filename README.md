# kotlin-retrofit

1. 用kotlin语言编写的retrofit网络请求封装库.
2. 支持get post(json) put delete download upload。
3. 支持表单(postForm、putForm)请求。
4. 支持文件上传、下载支持进度回调。
5. 支持在Activity销毁时，自动取消网络。
6. 支持一个页面多个请求，回调到一个地方。
7. 支持自定义多个BaseURL，支持Kotlin协程，同时请求多个接口。

### 引入

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
// 依赖
implementation 'com.github.wenkency:kotlin-retrofit:2.1.0'
// retrofit + okhttp + rxjava3
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation "com.squareup.okhttp3:okhttp:4.9.2"
implementation "com.squareup.okio:okio:2.8.0"
implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
implementation 'io.reactivex.rxjava3:rxjava:3.0.0'
// gson
implementation 'com.google.code.gson:gson:2.8.8'

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
 * Object回调
 */
fun postObj(activity: Activity?, callback: IObjectCallback, clazz: Class<*>) {
    MultiUrlPresenter.post(
        activity,
        "post",
        Bean("100"),
        ObjectCallback(callback, clazz)
    )
}

/**
 * get请求
 */
fun get(activity: Activity?, callback: ICallback) {
    RetrofitPresenter.get(activity, "https://www.baidu.com", callback)
}
/**
 * post请求
 */
fun post(activity: Activity?, callback: ICallback) {
    RetrofitPresenter.post(activity, "post", Bean("100"), callback)
} 


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

### 多个BaseUrl和自定义OkHttpClient配置

```
/**
 * 普通网络请求,继承IRetrofit
 */
object RetrofitPresenter : IRetrofit {

    /**
     * 可以根据URL获取请求接口
     */
    override fun getService(): RestService {
        return RestCreator.getService()
    }
}

/**
 * 自定义BaseURL和OkHttpClient
 */
class MultiUrlPresenter : IRetrofit {
    override fun getService(): RestService {
        return RestCreator.getService("http://www.baidu.com/", OkHttpClient());
    }
}

```