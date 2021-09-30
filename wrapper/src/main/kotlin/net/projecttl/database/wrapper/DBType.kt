package net.projecttl.database.wrapper

enum class DBType(val sql_type: String) {
    SQLITE("jdbc:sqlite"),
    MYSQL("jdbc:mysql"),
    MONGO("mongo")
}