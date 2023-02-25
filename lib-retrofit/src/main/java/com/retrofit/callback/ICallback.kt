package com.retrofit.callback

import com.retrofit.core.RestClient
import java.io.File
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * 回调接口定义
 */
interface ICallback {
    fun onBefore(client: RestClient) {}

    fun onAfter() {}

    fun onError(code: Int, message: String, client: RestClient) {}

    fun onSuccess(result: String, client: RestClient) {}

    /**
     * 文件上传的进度
     */
    fun onProgress(progress: Float, current: Float, total: Float) {}

    /**
     * 文件上下载成功
     */
    fun onSuccess(file: File) {

    }
}

/**
 * 获取泛型的实际类型
 */
fun Any.getType(): Type {
    return getType(javaClass.genericSuperclass!!)
}

fun getType(type: Type): Type {
    when (type) {
        is ParameterizedType -> {
            return getGenericType(type)
        }
        is TypeVariable<*> -> {
            return getType(type.bounds[0])
        }
    }
    return type
}

fun getGenericType(type: ParameterizedType): Type {
    if (type.actualTypeArguments.isEmpty()) return type
    val actualType = type.actualTypeArguments[0]
    when (actualType) {
        !is Class<*> -> {
            return ParameterTypeImpl(
                List::class.java,
                (actualType as ParameterizedType).actualTypeArguments[0]
            )
        }
        is ParameterizedType -> {
            return actualType.rawType
        }
        is GenericArrayType -> {
            return actualType.genericComponentType
        }
        is TypeVariable<*> -> {
            return getType(actualType.bounds[0])
        }
    }
    return actualType
}

class ParameterTypeImpl(private val clazz: Class<*>, private val type: Type) : ParameterizedType {
    override fun getRawType(): Type {
        return clazz
    }

    override fun getOwnerType(): Type? {
        return clazz
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(type)
    }
}