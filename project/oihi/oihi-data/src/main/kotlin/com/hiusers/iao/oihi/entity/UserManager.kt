package com.hiusers.iao.oihi.entity

import com.hiusers.iao.oihi.entity.container.RoleContainer
import com.hiusers.iao.oihi.entity.container.UserContainer
import com.hiusers.iao.oihi.entity.container.UserPermContainer
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf

object UserManager {

    val Database.users get() = this.sequenceOf(UserContainer)

    val Database.roles get() = this.sequenceOf(RoleContainer)

    val Database.userPerms get() = this.sequenceOf(UserPermContainer)

}