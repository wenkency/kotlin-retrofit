package com.lven.retrofit.callback

import com.lven.retrofit.core.RestCreator
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * 泛型解析：实际开发自己解析最好
 */
abstract class BeanCallback<T> : ICallback {
    override fun onSuccess(result: String) {
        // 直接返回String类型
        if (this.getType().toString().contains("java.lang.String")) {
            onSucceed(result as T)
        } else {
            onSucceed(RestCreator.gson.fromJson(result, this.getType()))
        }
    }

    abstract fun onSucceed(t: T)
}

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