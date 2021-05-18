package com.example.mystudy.coil.request

import com.example.mystudy.coil.target.ViewTarget
import kotlinx.coroutines.Job
import java.util.*

interface Disposable {

    val isDisposed: Boolean

    fun dispose()

    suspend fun await()
}

class BaseTargetDisposable(private val job: Job) : Disposable {
    override val isDisposed: Boolean
        get() = !job.isActive

    override fun dispose() {
        if (isDisposed) return
        job.cancel()
    }

    override suspend fun await() {
        if (isDisposed) return
        job.join()
    }

}

class ViewTargetDisposable(private val requestId: UUID, private val target: ViewTarget<*>) : Disposable {
    override val isDisposed: Boolean
        get() = false
    override fun dispose() {
        if (isDisposed) {
            return
        }
//        target.view.requestManager.clearCurrentRequest()
    }

    override suspend fun await() {
        if (isDisposed) {
            return
        }
//        target.view.requestManager.currentRequestJob?.join()
    }

}