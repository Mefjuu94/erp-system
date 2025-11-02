package model

import classModels.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

//Zawiera definicję tabeli w bazie danych (Exposed ORM). To mówi, jak wygląda tabela users w PostgreSQL.
object UserTable : IntIdTable("users") {
    val username = varchar("username", 50)
    val surname = varchar("surname", 50)
    val role = varchar("role", 50)
    val hours = varchar("hours", 50)
}

//zamienia wiersz z bazy (ResultRow) na obiekt User.
fun ResultRow.toUser(): User = User(
    id = this[UserTable.id].value,
    username = this[UserTable.username],
    surname = this[UserTable.surname],
    role = this[UserTable.role],
    hours = this[UserTable.hours],
)
