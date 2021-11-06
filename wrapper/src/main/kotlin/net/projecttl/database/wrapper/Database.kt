package net.projecttl.database.wrapper

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.jetbrains.annotations.NotNull
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun databaseOf(connection: SQLite): Database {
    return Database(connection)
}

fun databaseOf(connection: MySQL): Database {
    return Database(connection)
}

fun databaseOf(connection: Mongo): Database {
    return Database(connection)
}

class Database {

    constructor(connection: SQLite) {
        dbType = DBType.SQLITE
        url = connection.filePath
        database = connection.table
    }

    constructor(connection: MySQL) {
        dbType = DBType.MYSQL
        url = connection.url
        port = connection.port
        database = connection.database_name
    }

    constructor(connection: Mongo) {
        dbType = DBType.MONGO
        url = connection.url
        port = connection.port
        database = connection.database_name
    }

    // USER CERTIFICATE
    private var username: String? = null
    private var password: String? = null

    // DATABASE CONFIG
    private var dbType: DBType? = null
    private var url: String? = null
    private var port : Int? = null
    private var database: String? = null

    // MONGO CLIENT ONLY
    private lateinit var mongoClient: MongoClient

    // Non-Connection(SQLite) Only
    fun connect() {
        if (dbType == DBType.MYSQL || dbType == DBType.MONGO) {
            println("This is connection type database!")
            return
        } else {
            println("Try connect to SQLite")
            Class.forName("org.sqlite.JDBC")

            try {
                connection = DriverManager.getConnection("${dbType?.db_type}:${url}")
                println("Connected ${dbType?.db_type}:$url")
            } catch (exception: Exception) {
                exception.printStackTrace()
            } catch (exception: SQLException) {
                exception.printStackTrace()
            }
        }
    }

    fun connect(@NotNull username: String, @NotNull password: String) {
        when (dbType) {
            DBType.SQLITE -> {
                println("Cannot connect ${dbType?.db_type}://${url}. Because this function is not Non-Connection only!")
                return
            }

            DBType.MYSQL -> {
                println("Selected MySQL")
                Class.forName("com.mysql.cj.jdbc.Driver")

                this.username = username
                this.password = password

                try {
                    connection = DriverManager.getConnection("${dbType?.db_type}://${url}:${port}/${database}", username, password)
                    println("Connected ${dbType?.db_type}://$url:$port/$database")
                } catch (exception: Exception) {
                    exception.printStackTrace()
                } catch (exception: SQLException) {
                    exception.printStackTrace()
                }
            }

            DBType.MONGO -> {
                mongoClient = MongoClients.create("${dbType?.db_type}://${url}:${port}")
                mongoDB = mongoClient.getDatabase(database!!)
            }
        }
    }

    fun reconnect() {
        when (dbType) {
            DBType.SQLITE -> {
                try {
                    if (!connection.isClosed) {
                        println("Database is already connected")
                    } else {
                        println("Try to reconnect to database")
                        connection = DriverManager.getConnection("${dbType?.db_type}:${url}", username, password)

                        println("Reconnected ${dbType?.db_type}:$url")
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                } catch (exception: SQLException) {
                    exception.printStackTrace()
                }
            }

            DBType.MYSQL -> {
                try {
                    if (!connection.isClosed) {
                        println("Database is already connected")
                    } else {
                        println("Try to reconnect to database")
                        connection = DriverManager.getConnection("${dbType?.db_type}://${url}:${port}/${database}", username, password)

                        println("Reconnected ${dbType?.db_type}://$url:$port/$database")
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                } catch (exception: SQLException) {
                    exception.printStackTrace()
                }
            }

            DBType.MONGO -> {
                mongoClient = MongoClients.create("${dbType?.db_type}://${url}:${port}")
                mongoDB = mongoClient.getDatabase(database!!)
            }
        }
    }

    fun disconnect() {
        when (dbType) {
            DBType.SQLITE, DBType.MYSQL -> {
                try {
                    if (!connection.isClosed) {
                        println("Disconnecting...")
                        connection.close()
                    }
                } catch (exception: SQLException) {
                    exception.printStackTrace()
                } finally {
                    println("Database successful disconnected")
                }
            }

            DBType.MONGO -> {
                println("Disconnecting...")
                mongoClient.close()

                println("Database successful disconnected")
            }
        }
    }

    fun getConnection(): Connection {
        return connection
    }

    fun getMongoDB(): MongoDatabase {
        return mongoDB
    }

    companion object {
        lateinit var connection: Connection // MySQL or SQLite Only
        lateinit var mongoDB: MongoDatabase // Mongo DB Only
    }
}