package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.entity.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransDao {

    @Upsert
    suspend fun insertOrUpdate(transEntity: TransEntity)

    @Delete
    suspend fun delete(transEntity: TransEntity)

    @Query(
        value = """SELECT * FROM Transactions
            WHERE personId =:personId 
            ORDER BY 
            CASE :sort WHEN 0 THEN created END ASC,
            CASE :sort WHEN 1 THEN date END ASC"""
    )
    fun allTransactionsByPersonId(personId: Int, sort: Int): Flow<List<TransEntity>>

    @Query(value = "SELECT * FROM Transactions WHERE created IN (:dates)")
    fun allTransactionsByBetweenDates(dates: List<Long>): Flow<List<TransEntity>>

    @Query(value = "SELECT * FROM Transactions WHERE created = :date")
    fun allTransactionsByDate(date: Long): Flow<List<TransEntity>>

    @Query(
        value = """SELECT * FROM Transactions WHERE personId = :personId 
            AND (date LIKE '%' || :searchQuery || '%' 
            OR description LIKE '%' || :searchQuery || '%' 
            OR amount LIKE '%' || :searchQuery || '%') 
            ORDER BY created ASC"""
    )
    fun allTransactionsSearch(personId: Int, searchQuery: String): Flow<List<TransEntity>>

    @Query(value = "SELECT * FROM Transactions")
    fun allTransactions(): Flow<List<TransEntity>>

    @Query(value = "SELECT * FROM Transactions WHERE id =:transId")
    fun transactionById(transId: Int): Flow<TransEntity>

    @Transaction
    @Query(
        value = """SELECT 
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum 
            FROM Transactions WHERE personId = :personId"""
    )
    fun balanceByPerson(personId: Int): Flow<TransSumModel>

    @Transaction
    @Query(
        value = """SELECT
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum
            FROM Transactions"""
    )
    fun balanceByAllAccounts(): Flow<TransSumModel>
}