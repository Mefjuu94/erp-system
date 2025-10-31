package model

import classModels.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object UserTable : IntIdTable("users") {
    val username = varchar("username", 50)
    val email = varchar("email", 100)
}

fun ResultRow.toUser(): User = User(
    id = this[UserTable.id].value,
    username = this[UserTable.username],
    email = this[UserTable.email]
)
