package com.hiusers.iao.router

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hiusers.iao.api.http.router.annotations.BaseRouter
import com.hiusers.iao.api.http.router.annotations.NodeRouter
import com.hiusers.iao.api.http.router.annotations.PathParam
import com.hiusers.iao.api.http.router.type.ParamType
import com.hiusers.iao.api.http.router.type.RequestType
import com.hiusers.iao.api.manager.DatabaseManager.database
import com.hiusers.iao.api.manager.SecurityManager.checkPassword
import com.hiusers.iao.api.manager.SecurityManager.hashPassword
import com.hiusers.iao.oihi.entity.UserManager.users
import com.hiusers.iao.oihi.entity.data.UserInfo
import com.hiusers.iao.router.body.AuthBody
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

/**
 * 验证路由
 */
@BaseRouter("/auth")
object AuthRouter {

    /**
     * 登录请求
     */
    @NodeRouter("/login", RequestType.POST)
    fun login(@PathParam("data", ParamType.BODY) body: AuthBody): SaResult {
        val user = database?.users?.find {
            it.name eq body.name
        }
        return if (user != null && body.password.checkPassword(user.password)) {
            StpUtil.login(user.id)
            return SaResult.data(StpUtil.getTokenInfo())
        } else {
            SaResult.error("身份验证不通过")
        }
    }


    /**
     * 登出请求
     */
    @NodeRouter("/logout", RequestType.POST)
    fun logout(): SaResult {
        StpUtil.logout()
        return SaResult.ok()
    }

    /**
     * 注册
     */
    @NodeRouter("/register", RequestType.POST)
    fun register(@PathParam("data", ParamType.BODY) body: AuthBody): SaResult {
        val user = UserInfo {
            name = name
            password = password.hashPassword()
        }
        val userId = database?.users?.add(user) ?: return SaResult.error("注册失败")
        StpUtil.login(userId)
        return SaResult.data(StpUtil.getTokenInfo())
    }

}