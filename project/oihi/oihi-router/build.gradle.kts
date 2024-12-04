dependencies {
    compileOnly(project(":project:module-util"))
    compileOnly(project(":project:module-api"))
    compileOnly(project(":project:oihi:oihi-data"))
}

// 子模块
taboolib { subproject = true }