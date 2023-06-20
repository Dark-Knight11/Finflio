package com.finflio.feature_authentication.domain.use_case

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_authentication.data.models.AuthResponse
import com.finflio.feature_authentication.domain.repository.AuthRepository
import com.finflio.feature_authentication.domain.util.InvalidAuthRequestException
import com.finflio.feature_authentication.domain.util.loginErrors
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    @Throws(InvalidAuthRequestException::class)
    suspend fun login(email: String, password: String): Flow<Resource<AuthResponse>> {
        loginErrors(email, password)
        return authRepository.login(email, password)
    }
}