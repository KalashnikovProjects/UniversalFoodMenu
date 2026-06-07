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

object ScreenStyleFromStringSerializer : KSerializer<ScreenStyleDTO> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ScreenStyleFromString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ScreenStyleDTO {
        val jsonString = decoder.decodeString()
        
        if (jsonString.isBlank() || jsonString == "{}") {
            return ScreenStyleDTO()
        }

        val json = (decoder as? JsonDecoder)?.json ?: Json { ignoreUnknownKeys = true }
        return json.decodeFromString(ScreenStyleDTO.serializer(), jsonString)
    }

    override fun serialize(encoder: Encoder, value: ScreenStyleDTO) {
        val json = (encoder as? JsonEncoder)?.json ?: Json
        val jsonString = json.encodeToString(ScreenStyleDTO.serializer(), value)
        encoder.encodeString(jsonString)
    }
}