package org.jetbrains.ktor.sessions

import org.jetbrains.ktor.application.ApplicationCall
import kotlin.reflect.KClass

class SessionTrackerByValue(val type: KClass<*>, val serializer: SessionSerializer) : SessionTracker {
    suspend override fun load(call: ApplicationCall, transport: String?): Any? {
        return transport?.let { serializer.deserialize(it) }
    }

    suspend override fun store(call: ApplicationCall, value: Any): String {
        val serialized = serializer.serialize(value)
        return serialized
    }

    override fun validate(value: Any) {
        if (!type.javaObjectType.isAssignableFrom(value.javaClass)) {
            throw IllegalArgumentException("Value for this session tracker expected to be of type $type but was $value")
        }
    }

    suspend override fun clear(call: ApplicationCall) {
        // it's stateless, so nothing to clear
    }
}

