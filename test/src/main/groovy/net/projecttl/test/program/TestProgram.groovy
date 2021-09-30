package net.projecttl.test.program

import net.projecttl.database.wrapper.DBType
import net.projecttl.database.wrapper.Database

class TestProgram {
    static void main(String[] args) {
        while (1) {
            int num = (Math.random() * 2) as int
            println(num)

            def database = new Database(DBType.MYSQL, "db.projecttl.net", 3306, "TestDatabase")
            database.connect("projecttl", "Project338900@")
            Thread.sleep(3000)

            switch (num) {
                case 0:
                    database.disconnect()
                    break

                case 1:
                    database.reconnect()
                    break
            }
        }
    }
}
