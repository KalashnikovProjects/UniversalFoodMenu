package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

object StyleFromStringSerializer : KSerializer<StyleDTO> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StyleFromString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): StyleDTO {
        val jsonString = decoder.decodeString()
        
        if (jsonString.isBlank() || jsonString == "{}") {
            return StyleDTO()
        }

        val json = (decoder as? JsonDecoder)?.json ?: Json { ignoreUnknownKeys = true }
        return json.decodeFromString(StyleDTO.serializer(), jsonString)
    }

    override fun serialize(encoder: Encoder, value: StyleDTO) {
        val json = (encoder as? JsonEncoder)?.json ?: Json
        val jsonString = json.encodeToString(StyleDTO.serializer(), value)
        encoder.encodeString(jsonString)
    }
}