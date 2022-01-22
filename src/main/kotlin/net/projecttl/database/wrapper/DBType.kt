package net.projecttl.database.wrapper

enum class DBType(val db_type: String) {
    SQLITE("jdbc:sqlite"),
    MYSQL("jdbc:mysql"),
    MONGO("mongodb")
}