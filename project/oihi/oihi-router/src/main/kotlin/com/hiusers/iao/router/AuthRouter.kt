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
import com.hiusers.iao.router.body.RegisterBody
import com.hiusers.iao.router.result.ResultUserInfo
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
        if (body.name == null || body.password == null) {
            return SaResult.error("用户名或密码为空")
        }
        val user = database?.users?.find {
            it.name eq body.name
        }
        return if (user != null && body.password.checkPassword(user.password)) {
            StpUtil.login(user.id)
            val userInfo = StpUtil.getTokenInfo()
            if (userInfo.isLogin) {
                return SaResult.data(
                    ResultUserInfo(user.id.toString(), user.name, user.nickname, userInfo.tokenValue)
                )
            } else {
                return SaResult.error("登录失败")
            }
        } else {
            SaResult.error("身份验证不通过")
        }
    }

    /**
     * 获取用户信息
     */
    @NodeRouter("/info", RequestType.GET)
    fun info(): SaResult {
        val userInfo = StpUtil.getTokenInfo()
        if (userInfo == null) {
            return SaResult.error("用户未登录")
        } else {
            val id = userInfo.loginId as Long
            val user = database?.users?.find { it.id eq id }
            return SaResult.data(
                ResultUserInfo(user?.id?.toString(), user?.name, user?.nickname, userInfo.tokenValue)
            )
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
    fun register(@PathParam("data", ParamType.BODY) body: RegisterBody): SaResult {
        if (body.name == null || body.password == null || body.nickname == null) {
            return SaResult.error("参数错误")
        }
        val findUser = database?.users?.find {
            it.name eq body.name
        }
        if (findUser != null) {
            return SaResult.error("用户名已存在")
        }
        val user = UserInfo {
            name = body.name
            nickname = body.nickname
            password = body.password.hashPassword()
        }
        database?.users?.add(user) ?: return SaResult.error("注册失败")
        StpUtil.login(user.id)
        val userInfo = StpUtil.getTokenInfo()
        return SaResult.data(ResultUserInfo(user.id.toString(), user.name, user.nickname, userInfo.tokenValue))
    }

}