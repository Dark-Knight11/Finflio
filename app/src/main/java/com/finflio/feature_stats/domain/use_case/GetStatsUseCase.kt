package com.finflio.feature_stats.domain.use_case

import android.util.Log
import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_stats.domain.mapper.toStatsData
import com.finflio.feature_stats.domain.model.StatsData
import com.finflio.feature_stats.domain.repository.StatsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetStatsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(): Flow<StatsData?> {
        return channelFlow {
            repository.getStats().collectLatest { res ->
                when (res.status) {
                    Resource.Status.SUCCESS -> {
                        send(res.data?.stats?.toStatsData())
                        close()
                    }

                    Resource.Status.ERROR -> {
                        Log.i(this.toString(), res.message.toString())
                        close()
                    }

                    Resource.Status.LOADING -> {
                        println("loading...")
                    }
                }
            }
        }
    }
}