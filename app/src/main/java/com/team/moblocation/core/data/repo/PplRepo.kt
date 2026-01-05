package com.team.moblocation.core.data.repo

import com.team.moblocation.core.data.model.Ppl
import kotlinx.coroutines.flow.Flow

interface PplRepo {
    suspend fun createPpl(ppl: Ppl)
    suspend fun getPplByUid(uid: String): Ppl?
    suspend fun getAllPpl(): Flow<List<Ppl>>

    companion object {
        private var instance: PplRepo? = null
        fun getInstance(): PplRepo {
            if (instance == null) {
                instance = PplRepoRealImpl()
            }
            return instance!!
        }
    }
}