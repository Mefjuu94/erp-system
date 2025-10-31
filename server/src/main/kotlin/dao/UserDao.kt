package dao

import classModels.User
import model.UserTable
import model.toUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserDao {
    fun create(user: User): User {
        val id = transaction {
            UserTable.insertAndGetId {
                it[username] = user.username
                it[email] = user.email
            }.value
        }
        return user.copy(id = id)
    }

    fun getAll(): List<User> = transaction {
        UserTable.selectAll().map { it.toUser() }
    }
}
