package com.xinto.opencord.gateway

import com.github.materiiapps.enumutil.FromValue

@FromValue
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

    companion object
}

val CloseCode?.canReconnect: Boolean
    get() {
        return when (this) {
            CloseCode.Unknown -> false
            CloseCode.UnknownError -> true
            CloseCode.UnknownOpCode -> true
            CloseCode.DecodeError -> true
            CloseCode.NotAuthorized -> true
            CloseCode.AuthenticationFailed -> false
            CloseCode.AlreadyAuthenticated -> true
            CloseCode.InvalidSequenceNumber -> true
            CloseCode.RateLimited -> true
            CloseCode.SessionTimedOut -> true
            else -> false
        }
    }