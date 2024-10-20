package com.twakeapps.roomdbsqlite

import android.app.Application
import com.twakeapps.roomdbsqlite.db.AppDatabase

class App: Application()
{

    companion object{

        lateinit var db:AppDatabase

    }

    override fun onCreate()
    {
        super.onCreate()

        db = AppDatabase.getDb(applicationContext)

    }


}