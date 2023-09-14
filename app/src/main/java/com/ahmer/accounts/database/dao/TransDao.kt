package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransDao {

    @Upsert
    suspend fun insertOrUpdate(transEntity: TransEntity)

    @Delete
    suspend fun delete(transEntity: TransEntity)

    @Query("SELECT * FROM Transactions WHERE id =:transId")
    fun getAllTransById(transId: Int): Flow<TransEntity>

    @Query("SELECT * FROM Transactions WHERE personId =:personId ORDER BY created ASC")
    fun getAllTransByPersonId(personId: Int): Flow<List<TransEntity>>

    @Query("SELECT * FROM Transactions WHERE personId = :personId AND (date LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' OR amount LIKE '%' || :searchQuery || '%') ORDER BY created ASC")
    fun getAllTransByPersonIdWithSearch(personId: Int, searchQuery: String): Flow<List<TransEntity>>

    @Transaction
    @Query("SELECT SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum, SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum FROM Transactions WHERE (type IN('Credit', 'Debit') AND personId = :personId)")
    fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel>

    @Transaction
    @Query("SELECT SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum, SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum FROM Transactions WHERE (type IN('Credit', 'Debit'))")
    fun getAllAccountsBalance(): Flow<TransSumModel>
}