package com.xinto.opencord.gateway

enum class CloseCode(val code: Int) {
    Unknown(-1),
    UnknownError(4000),
    UnknownOpCode(4001),
    DecodeError(4002),
    NotAuthorized(4003),
    AuthenticationFailed(4004),
    AlreadyAuthenticated(4005),
    InvalidSequenceNumber(4007),
    RateLimited(4008),
    SessionTimedOut(4009);

    val canReconnect: Boolean
        get() {
            return when (this) {
                Unknown -> false
                UnknownError -> true
                UnknownOpCode -> true
                DecodeError -> true
                NotAuthorized -> true
                AuthenticationFailed -> false
                AlreadyAuthenticated -> true
                InvalidSequenceNumber -> true
                RateLimited -> true
                SessionTimedOut -> true
            }
        }

    companion object {
        fun fromCode(code: Int): CloseCode {
            return when (code) {
                UnknownError.code -> UnknownError
                UnknownOpCode.code -> UnknownOpCode
                DecodeError.code -> DecodeError
                NotAuthorized.code -> NotAuthorized
                AuthenticationFailed.code -> AuthenticationFailed
                AlreadyAuthenticated.code -> AlreadyAuthenticated
                InvalidSequenceNumber.code -> InvalidSequenceNumber
                RateLimited.code -> RateLimited
                SessionTimedOut.code -> SessionTimedOut
                else -> Unknown
            }
        }
    }
}