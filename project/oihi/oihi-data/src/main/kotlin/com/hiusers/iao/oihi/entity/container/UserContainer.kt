package com.hiusers.iao.oihi.entity.container

import com.hiusers.iao.api.database.annotations.CreateTable
import com.hiusers.iao.oihi.entity.data.UserInfo
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

@CreateTable
object UserContainer : Table<UserInfo>("user_info") {

    val id = long("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val nickname = varchar("nickname").bindTo { it.nickname }

    val password = varchar("password").bindTo { it.password }

}