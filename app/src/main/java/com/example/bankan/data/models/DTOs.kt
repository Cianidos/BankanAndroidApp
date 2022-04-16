package com.example.bankan.data.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.time.Instant
import java.util.*

data class BoardData(val info: BoardInfo, val content: List<ListData>)

@Entity
data class BoardInfo(
    val name: String,
    val description: String = "",
    val isOpen: Boolean = false,
    val creationDate: Date = Date.from(Instant.EPOCH),
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)

data class ListData(val info: ListInfo, val content: List<CardInfo>)

@Entity
data class ListInfo(
    val name: String,
    val description: String = "",

    val boardId: Int = -1,
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)

private val module = SerializersModule {
    contextual(object : KSerializer<Color> {
        override fun deserialize(decoder: Decoder): Color = Color(decoder.decodeString().toULong())

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("compose.Color", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Color) {
            encoder.encodeString(value.value.toString())
        }

    })
}

val JsonCoder = Json { serializersModule = module }

@Serializable
data class CardTag(
    val name: String,
    @Contextual
    val color: Color
)

@Entity
data class CardInfo(
    val name: String,
    val description: String = "",
    val tags: List<CardTag> = emptyList(),

    val listId: Int = -1,
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)
