package io.github.illusion.mobileapp.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke

class SafeStorage(private val kSafe: KSafe) {
    var userToken: String by kSafe("", "AUTH_TOKEN")
    var userID: String by kSafe("", "USER_ID")
}