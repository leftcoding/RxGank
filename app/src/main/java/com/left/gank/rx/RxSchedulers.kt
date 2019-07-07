package com.left.gank.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun io(): Scheduler {
    return Schedulers.io()
}

fun mainThread(): Scheduler {
    return AndroidSchedulers.mainThread()
}

fun computation(): Scheduler {
    return Schedulers.computation()
}

fun newThread(): Scheduler {
    return Schedulers.newThread()
}

fun single(): Scheduler {
    return Schedulers.single()
}

