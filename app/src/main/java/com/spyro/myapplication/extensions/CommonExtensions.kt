package com.spyro.myapplication.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeNotNull(
    owner: LifecycleOwner,
    observer: (T) -> Unit,
) {
    observe(owner, NonNullObserver(observer))
}

class NonNullObserver<V>(val block: (V) -> Unit) : Observer<V> {
    override fun onChanged(t: V?) {
        t?.let { block(t) }
    }
}
