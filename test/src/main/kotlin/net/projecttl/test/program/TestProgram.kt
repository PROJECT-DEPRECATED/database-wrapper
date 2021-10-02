package net.projecttl.test.program

import net.projecttl.database.wrapper.Mongo
import net.projecttl.database.wrapper.MySQL
import net.projecttl.database.wrapper.SQLite
import net.projecttl.database.wrapper.databaseOf

fun main() {
    databaseOf(MySQL("db.projecttl.net", null, "TestDatabase")).apply {
        connect(INFO.username, INFO.password)

        Thread.sleep(10000) // 10 Seconds
        disconnect()

        Thread.sleep(3000) // 3 Seconds
        reconnect()

        Thread.sleep(2000) // 2 Seconds
        disconnect()
    }

    println("Mongo Test")
    databaseOf(Mongo("db.projecttl.net", null, "TestDatabase")).let {
        it.connect(INFO.username, INFO.password)

        Thread.sleep(10000) // 10 Seconds
        it.disconnect()

        Thread.sleep(3000) // 3 Seconds
        it.reconnect()

        Thread.sleep(2000) // 2 Seconds
        it.disconnect()

        it
    }.connect()

    databaseOf(SQLite("test.sqlite", "test")).apply {
        connect()

        Thread.sleep(10000)
        disconnect()

        Thread.sleep(3000)
        reconnect()

        Thread.sleep(2000)
        disconnect()

        connect(INFO.username, INFO.password)
    }
}