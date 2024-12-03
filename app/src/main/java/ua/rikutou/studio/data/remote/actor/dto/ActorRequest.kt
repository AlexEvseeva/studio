package ua.rikutou.studio.data.remote.actor.dto

data class ActorRequest(
    val actorId: Long? = null,
    val name: String,
    val nickName: String? = null,
    val role: String? = null,
    val studioId: Long,
)