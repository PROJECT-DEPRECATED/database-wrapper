package net.projecttl.database.wrapper

import net.projecttl.database.wrapper.DBType

data class SQLite(val filePath: String, val table: String)
data class MySQL(val url: String, var port: Int?, val database_name: String) {
    init {
        if (port == null) {
            port = 3306
        }
    }
}

data class Mongo(val url: String, var port: Int?, val database_name: String) {
    init {
        if (port == null) {
            port = 27017
        }
    }
}