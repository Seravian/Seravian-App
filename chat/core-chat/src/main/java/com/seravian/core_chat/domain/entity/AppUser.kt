package com.seravian.core_chat.domain.entity

data class AppUser(
    val fullName : String ?= null,
    val email : String ?= null,
    val uId : String ?= null,
){
    companion object{
        const val COLLECTION_NAME = "Users"
    }
}
