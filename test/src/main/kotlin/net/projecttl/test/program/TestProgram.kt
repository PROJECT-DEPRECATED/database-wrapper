package net.projecttl.test.program

import net.projecttl.database.wrapper.Mongo
import net.projecttl.database.wrapper.databaseOf

fun main() {
    val database = databaseOf(Mongo("db.projecttl.net", null, "TestDatabase"))
    database.connect(INFO.username, INFO.password)

    while (true) {
        Thread.sleep(3000)
        database.disconnect()

        Thread.sleep(3000)
        database.reconnect()
    }
}