package com.hiusers.iao.oihi.entity.data

import org.ktorm.entity.Entity

interface Role: Entity<Role> {

    companion object : Entity.Factory<Role>()

    val id: Long

    var name: String

    var user: UserInfo

}
