package com.xinto.partialgen

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(PartialValue.Serializer::class)
public sealed class PartialValue<out T> {

    public class Value<out T>(public val value: T) : PartialValue<T>() {
        override fun toString(): String = value.toString()

        override fun equals(other: Any?): Boolean {
            val value = other as? Value<*> ?: return false
            return value.value == this.value
        }

        override fun hashCode(): Int = value.hashCode()
    }

    public object Missing : PartialValue<Nothing>() {
        override fun toString(): String = "Missing"

        override fun equals(other: Any?): Boolean = other is Missing

        override fun hashCode(): Int = 0
    }

    internal class Serializer<T>(
        private val valueSerializer: KSerializer<T>
    ) : KSerializer<PartialValue<T?>> {

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

public inline fun <T> PartialValue<T>.getOrElse(block: () -> T): T {
    return when (this) {
        is PartialValue.Missing -> block()
        is PartialValue.Value -> value
    }
}

public fun <T> PartialValue<T>.getOrNull(): T? = getOrElse { null }

public inline fun <T, R> PartialValue<T>.mapToPartial(block: (T) -> R): PartialValue<R> {
    return when (this) {
        is PartialValue.Missing -> this
        is PartialValue.Value -> PartialValue.Value(block(value))
    }
}