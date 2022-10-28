package com.github.kr328.clash.service.data

import androidx.room.*
import java.util.*

@Dao
@TypeConverters(Converters::class)
interface SubscriptionUserInfoDao {
    @Query("SELECT * FROM subscription_user_info WHERE uuid = :uuid")
    suspend fun queryByUUID(uuid: UUID): SubscriptionUserInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setInfo(info: SubscriptionUserInfo)
}