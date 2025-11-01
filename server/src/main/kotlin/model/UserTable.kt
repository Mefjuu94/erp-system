package model

import classModels.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

//Zawiera definicję tabeli w bazie danych (Exposed ORM). To mówi, jak wygląda tabela users w PostgreSQL.
object UserTable : IntIdTable("users") {
    val username = varchar("username", 50)
    val email = varchar("email", 100)
}

//zamienia wiersz z bazy (ResultRow) na obiekt User.
fun ResultRow.toUser(): User = User(
    id = this[UserTable.id].value,
    username = this[UserTable.username],
    email = this[UserTable.email]
)
