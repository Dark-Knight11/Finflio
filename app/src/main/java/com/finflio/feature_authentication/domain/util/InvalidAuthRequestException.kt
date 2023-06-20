package com.finflio.feature_authentication.domain.util

import com.finflio.feature_authentication.domain.util.InvalidAuthRequestException.Companion.EMAIL_REGEX
import java.util.regex.Pattern

class InvalidAuthRequestException(message: String) : Exception(message) {
    companion object {
        const val EMAIL_REGEX =
            "[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    }
}

fun loginErrors(email: String?, password: String?) {
    if (email.isNullOrBlank()) {
        throw InvalidAuthRequestException("Email field can't be empty.")
    } else if (!Pattern.matches(EMAIL_REGEX, email)) {
        throw InvalidAuthRequestException("Email is Invalid")
    } else if (password.isNullOrBlank()) {
        throw InvalidAuthRequestException("Password field can't be empty.")
    } else if (password.length !in (8..50)) {
        throw InvalidAuthRequestException(
            "Password should be of min 8 and max 50 character in length"
        )
    }
}

fun registerErrors(name: String?, email: String?, password: String?, confirmPassword: String?) {
    if (name.isNullOrBlank()) {
        throw InvalidAuthRequestException("Name field can't be empty.")
    } else if (email.isNullOrBlank()) {
        throw InvalidAuthRequestException("Email field can't be empty.")
    } else if (!Pattern.matches(EMAIL_REGEX, email)) {
        throw InvalidAuthRequestException("Email is Invalid")
    } else if (password.isNullOrBlank()) {
        throw InvalidAuthRequestException("Password field can't be empty.")
    } else if (password.length !in (8..50)) {
        throw InvalidAuthRequestException(
            "Password should be of min 8 and max 50 character in length"
        )
    } else if (password != confirmPassword) {
        throw InvalidAuthRequestException(
            "To create a valid password, both the password and confirm password fields value must be matched"
        )
    }
}