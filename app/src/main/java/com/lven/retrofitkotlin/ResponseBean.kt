package com.lven.retrofitkotlin

data class ResponseBean(
    val args: Args,
    val `data`: String,
    val files: Files,
    val form: Form,
    val headers: Headers,
    val json: Json,
    val origin: String,
    val url: String
)

class Args(
)

class Files(
)

class Form(
)

data class Headers(
    val `Accept-Encoding`: String,
    val `Content-Length`: String,
    val `Content-Type`: String,
    val Host: String,
    val `User-Agent`: String,
    val `X-Amzn-Trace-Id`: String
)

data class Json(
    val data: List<String>,
    val id: String
)