package com.xinto.partialgen

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(PartialValue.Serializer::class)
sealed class PartialValue<out T> {

    class Value<T>(val value: T): PartialValue<T>()
    class Missing<out T> : PartialValue<T>()

    internal class Serializer<T>(
        private val valueSerializer: KSerializer<T>
    ): KSerializer<PartialValue<T?>> {

        override val descriptor: SerialDescriptor
            get() = valueSerializer.descriptor

        override fun serialize(encoder: Encoder, value: PartialValue<T?>) {
            if (value is Value) {
                if (value.value != null) {
                    encoder.encodeNotNullMark()
                    encoder.encodeSerializableValue(valueSerializer, value.value)
                } else {
                    encoder.encodeNull()
                }
            }
        }

        override fun deserialize(decoder: Decoder): PartialValue<T?> {
            val value = if (!decoder.decodeNotNullMark()) {
                decoder.decodeNull()
            } else {
                decoder.decodeSerializableValue(valueSerializer)
            }

            return Value(value)
        }
    }
}

inline fun <T> PartialValue<T>.getOrElse(block: () -> T): T {
    return when (this) {
        is PartialValue.Missing -> block()
        is PartialValue.Value -> value
    }
}

fun <T> PartialValue<T>.getOrNull() = getOrElse { null }

inline fun <T, R> PartialValue<T>.mapToPartial(block: (T) -> R): PartialValue<R> {
    return when (this) {
        is PartialValue.Missing -> PartialValue.Missing()
        is PartialValue.Value -> PartialValue.Value(block(value))
    }
}