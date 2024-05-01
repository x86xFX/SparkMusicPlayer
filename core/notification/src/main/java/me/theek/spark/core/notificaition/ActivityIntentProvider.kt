package me.theek.spark.core.notificaition

import android.content.Intent

interface ActivityIntentProvider {
    fun provideMainActivityIntent(): Intent
}