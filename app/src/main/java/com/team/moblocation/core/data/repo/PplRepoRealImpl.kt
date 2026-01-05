package com.team.moblocation.core.data.repo

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.team.moblocation.core.data.model.Ppl
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PplRepoRealImpl: PplRepo {
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override suspend fun createPpl(ppl: Ppl) {
        dbRef.child(ppl.uid).setValue(ppl).await()
    }

    override suspend fun getPplByUid(uid: String): Ppl? {
        return dbRef.child(uid)
            .get()
            .await()
            .getValue(Ppl::class.java)
    }

    override suspend fun getAllPpl() = callbackFlow {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ppl = mutableListOf<Ppl>()
                for(pplSnapShot in snapshot.children) {
                    val key = pplSnapShot.key
                    pplSnapShot.getValue(Ppl::class.java)?.let { item ->
                        if (key != null) {
                            ppl.add(item.copy(uid = key))
                        }
                    }
                }
                trySend(ppl)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose()
    }
}