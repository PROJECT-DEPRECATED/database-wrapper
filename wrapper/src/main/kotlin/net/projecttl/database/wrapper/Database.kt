package net.projecttl.database.wrapper

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.jetbrains.annotations.NotNull
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.properties.Delegates

fun databaseOf(filePath: String, table: String): Database {
    return Database(DBType.SQLITE, filePath, table)
}

fun databaseOf(url: String, port: Int, database: String): Database {
    return Database(DBType.MYSQL, url, port, database)
}

class Database {

    constructor(db_type: DBType, url: String, port: Int, database: String) {
        this.dbType = db_type
        this.url = url
        this.port = port
        this.database = database
    }

    constructor(db_type: DBType, filePath: String, table: String) {

    }

    private lateinit var username: String
    private lateinit var password: String

    private var dbType: DBType

    private lateinit var url: String
    private var port by Delegates.notNull<Int>()

    private lateinit var database: String

    private lateinit var mongoClient: MongoClient

    fun login(username: String, password: String) {
        this.username = username
        this.password = password
    }

    fun connect(@NotNull username: String, @NotNull password: String) {
        when (dbType) {
            DBType.SQLITE -> {
                println("Try connect to SQLite")
                Class.forName("org.sqlite.JDBC")
            }

            DBType.MYSQL -> {
                println("Selected MySQL")
                Class.forName("com.mysql.cj.jdbc.Driver")
            }

            DBType.MONGO -> {
                mongoClient = MongoClients.create("mongo://${url}:${port}")
                mongoDB = mongoClient.getDatabase(database)
            }
        }

        this.username = username
        this.password = password

        try {
            connection = DriverManager.getConnection("${dbType.sql_type}://${url}:${port}/${database}", username, password)
            println("Connected ${dbType.sql_type}://$url:$port/$database")
        } catch (exception: Exception) {
            exception.printStackTrace()
        } catch (exception: SQLException) {
            exception.printStackTrace()
        }
    }

    fun reconnect() {
        when (dbType) {
            DBType.SQLITE, DBType.MYSQL -> {
                try {
                    if (!connection.isClosed) {
                        println("Database is already connected")
                    } else {
                        println("Try to reconnect to database")
                        connection = DriverManager.getConnection("${dbType.sql_type}://${url}:${port}/${database}", username, password)

                        println("Reconnected ${dbType.sql_type}://$url:$port/$database")
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                } catch (exception: SQLException) {
                    exception.printStackTrace()
                }
            }

            DBType.MONGO -> {
                mongoClient = MongoClients.create("mongo://${url}:${port}")
                mongoDB = mongoClient.getDatabase(database)
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
                mongoClient.close()
            }
        }
    }

    init {
        dbType = dbTypes
    }

    companion object {
        private lateinit var dbTypes: DBType

        lateinit var connection: Connection // MySQL or SQLite Only
        lateinit var mongoDB: MongoDatabase // Mongo DB Only
    }
}