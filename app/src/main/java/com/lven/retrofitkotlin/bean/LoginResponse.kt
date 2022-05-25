package com.lven.retrofitkotlin.bean

class LoginResponse {
    var data = ""
    var errorCode = ""
    var errorMsg = ""
    override fun toString(): String {
        return "LoginResponse(data='$data', errorCode='$errorCode', errorMsg='$errorMsg')"
    }
}