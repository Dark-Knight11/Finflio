package com.finflio.feature_authentication.domain.use_case

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_authentication.data.models.AuthResponse
import com.finflio.feature_authentication.domain.repository.AuthRepository
import com.finflio.feature_authentication.domain.util.InvalidAuthRequestException
import com.finflio.feature_authentication.domain.util.registerErrors
import javax.inject.Inject
import kotlin.jvm.Throws
import kotlinx.coroutines.flow.Flow

class RegisterUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    @Throws(InvalidAuthRequestException::class)
    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<AuthResponse>> {
        registerErrors(name, email, password, confirmPassword)
        return authRepo.register(name, email, password)
    }
}