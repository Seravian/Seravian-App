package com.seravian.feat_local.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seravian.core_chat.data.entity.ChatDao
import com.seravian.core_chat.data.entity.ChatEntity
import com.seravian.core_chat.data.entity.DiagnosisEntity
import com.seravian.core_chat.data.entity.MessageEntity
import com.seravian.core_profile.data.local.ProfileDao
import com.seravian.core_profile.data.local.ProfileEntity
import com.seravian.feat_local.data.db.utils.ListStringConverters

@Database(
    entities = [ProfileEntity::class, ChatEntity::class, MessageEntity::class, DiagnosisEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListStringConverters::class)
abstract class SeravianDatabase: RoomDatabase() {
    abstract val profileDao: ProfileDao
    abstract val chatDao: ChatDao
}