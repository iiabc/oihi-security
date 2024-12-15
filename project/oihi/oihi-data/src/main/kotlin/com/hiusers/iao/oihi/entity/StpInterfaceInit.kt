package com.hiusers.iao.oihi.entity

import cn.dev33.satoken.SaManager
import com.hiusers.iao.oihi.entity.impl.StpInterfaceImpl
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object StpInterfaceInit {

    @Awake(LifeCycle.ENABLE)
    fun init() {
        SaManager.setStpInterface(StpInterfaceImpl())
    }

}