package classModels

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int? = null,
                val username: String,
                val surname: String,
                var role: String,
                var hours: String,
                val title: String)
