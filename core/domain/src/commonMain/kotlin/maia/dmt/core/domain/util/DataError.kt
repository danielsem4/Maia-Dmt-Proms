package maia.dmt.core.domain.util

sealed interface DataError: Error {
    enum class Remote: DataError {
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERVICE_UNAVAILABLE,
        SERIALIZATION_ERROR,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
}