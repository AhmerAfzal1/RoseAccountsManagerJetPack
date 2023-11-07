package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.model.PersonsBalanceModel
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun insertOrUpdate(personsEntity: PersonsEntity)
    suspend fun delete(personsEntity: PersonsEntity)
    fun getAllPersons(): Flow<List<PersonsEntity>>
    fun getAllPersonsSorted(query: String, sortOrder: SortOrder): Flow<List<PersonsBalanceModel>>
    fun getPersonById(personId: Int): Flow<PersonsEntity?>
    fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel>
    fun getAllAccountsBalance(): Flow<TransSumModel>
}