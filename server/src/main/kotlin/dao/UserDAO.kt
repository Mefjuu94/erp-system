package dao

import classModels.User
import model.UserTable
import model.toUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object UserDAO {
    fun create(user: User): User {
        var insertedUser: User? = null
        transaction {
            val id = UserTable.insertAndGetId {
                it[username] = user.username
                it[surname] = user.surname
                it[role] = user.role
                it[hours] = user.hours
                it[title] = user.title
            }.value
            insertedUser = user.copy(id = id)
        }
        return insertedUser!!
    }

    fun getAll(): List<User> = transaction {
        UserTable.selectAll().map { it.toUser() }
    }

    fun getById(id: Int): User? = transaction {
        UserTable.select { UserTable.id eq id }
            .map { it.toUser() }
            .singleOrNull()
    }

    fun getBySurname(surname: String): User? = transaction {
        UserTable.select { UserTable.surname eq surname }
            .map { it.toUser() }
            .singleOrNull()
    }

    fun getByRole(role: String): User? = transaction {
        UserTable.select { UserTable.role eq role }
            .map { it.toUser() }
            .singleOrNull()
    }

}
