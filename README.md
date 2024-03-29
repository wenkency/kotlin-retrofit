# kotlin-retrofit

1. 用kotlin语言编写的retrofit网络请求封装库.
2. 支持get post(json) put delete download upload。
3. 支持表单(postForm、putForm)请求。
4. 支持文件上传、下载支持进度回调。
5. 支持在Activity销毁时，自动取消网络。
6. 支持一个页面多个请求，回调到一个地方。
7. 支持自定义多个BaseURL，支持Kotlin协程，同时请求多个接口。
8. 支持MVVM ViewModel的写法，可以自动取消网络请求。

### 引入

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
// 依赖
implementation 'com.github.wenkency:kotlin-retrofit:3.2.5'
// retrofit + okhttp + rxjava3
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:adapter-rxjava3:2.9.0'
implementation "com.squareup.okhttp3:okhttp:4.10.0"
implementation "com.squareup.okio:okio:2.8.0"
implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
implementation 'io.reactivex.rxjava3:rxjava:3.0.0'
// gson
implementation 'com.google.code.gson:gson:2.8.9'

```

### 混淆

```
-keep class *{
@com.retrofit.api.FieldToJson <fields>;
}
```

### API服务核心类

```
RestCreator，可以构建出：ApiClient（普通方式）、RxClient（Rx方式）、SuspendClient（协程方式）
```

### Application初始化

```
// 清单加上这个
<application
       ...
        android:usesCleartextTraffic="true">
       ...
</application>

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 1. 初始化 参考Demo
        RestConfig.baseUrl("http://httpbin.org/")
                .debugUrl("http://httpbin.org/")
                .debug(true)
                .register(this);
    }
}
```
### 核心类API参考类
```
/**
 * 普通网络请求,如果有扩展就继承IRetrofit，参照这个类来写
 * 比如有多个BaseURL，重写getService()方法就可以了
 */
object ApiClient : IRetrofit {
    // 是不是Rx方式
    override fun isRxService(): Boolean {
        return false
    }

    // 是不是协程方式
    override fun isSuspendService(): Boolean {
        return false
    }

    // 公共请求头
    override fun commHeaders(): MutableMap<String, String>? {
        return null
    }

    // 公共请求参数
    override fun commParams(): MutableMap<String, Any>? {
        return null
    }

    // 请求转换
    override fun requestConvert(client: RestClient, data: String): String {
        // 有特殊需求，比如要Base64加密，就可以在这里统一处理
        // client 处理一些特殊的请求，要用client判断
        return data
    }

    // 响应转换
    override fun responseConvert(client: RestClient, data: String): String {
        // 有特殊需求，比如要Base64解密，就可以在这里统一处理
        // client 处理一些特殊的请求，要用client判断
        return data
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
    ApiClient.get(activity, "https://www.baidu.com", callback)
}
/**
 * post请求
 */
fun post(activity: Activity?, callback: ICallback) {
    ApiClient.post(activity, "post", Bean("100"), callback)
} 


/**
 * post请求
 */
private fun post() {
    ApiClient.post(activity, "post", Bean("100"),
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
 * 协程单独使用 同时请求两个接口
 */
private fun async() {
    // 这个是获取 协程的API 统一请求接口
    val service = RestCreator.getSuspendService()
    
    GlobalScope.launch(Dispatchers.Main) {
        Log.e("TAG", "async---1---")
        // asysnc 不阻塞 在后台开启协程 有返回值
        val getResult = async(Dispatchers.IO) {
            val response = service.get("get", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
            Log.e("TAG", "async---2---")
            // 返回请求结果
            "${response.body()?.string()}"
        }
        val postResult = async(Dispatchers.IO) {
            val response =
                service.postForm("post", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
            Log.e("TAG", "async---3---")
            // 返回请求结果
            "${response.body()?.string()}"
        }
        Log.e("TAG", "async---4---")

        // await() 等待返回结果
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
object ApiClient : IRetrofit {

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

### Mvvm测试

* Mvvm Activity

```
/**
 * MVVM写法，测试网络请求
 */
class BindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityBindingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_binding)
        binding.vm = getViewModel(BindingViewModel::class.java)
        // LiveData需要感知生命周期
        binding.lifecycleOwner = this
    }

    /**
     * 创建ViewModel
     */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)
    }
}
```

* ViewModel

```
/**
 * 销毁自动取消网络请求
 */
class BindingViewModel : NetViewModel() {

    // 绑定TextView
    var name = MutableLiveData("")

    // 请求网络
    fun requestNet() {
        // 这个要
        ApiClient.post(this, "post", Bean("100"),
            object : BeanCallback<String>() {
                override fun onSucceed(t: String) {
                    name.value = t
                }
            })
    }

    /**
     * 销毁自动取消网络请求
     */
    override fun onCleared() {
        // 取消网络请求
        CancelNetUtils.cancel(this)
    }
}
```