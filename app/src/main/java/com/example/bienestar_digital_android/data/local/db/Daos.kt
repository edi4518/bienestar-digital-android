package com.example.bienestar_digital_android.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditoriaDao {
    @Query("SELECT * FROM auditorias_diarias ORDER BY timestamp DESC LIMIT :limit")
    fun getUltimasAuditorias(limit: Int = 7): Flow<List<AuditoriaDiariaEntity>>

    @Query("SELECT * FROM auditorias_diarias WHERE fecha = :fecha LIMIT 1")
    suspend fun getAuditoriaPorFecha(fecha: String): AuditoriaDiariaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarAuditoria(auditoria: AuditoriaDiariaEntity)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM perfil_usuario WHERE id = 1 LIMIT 1")
    fun getPerfilUsuario(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPerfil(perfil: UserProfileEntity)
}

@Dao
interface MinijuegoRecordDao {
    @Query("SELECT * FROM records_minijuegos WHERE juegoNombre = :juegoNombre ORDER BY puntajeMaximo DESC LIMIT 1")
    fun getRecordMaximo(juegoNombre: String): Flow<MinijuegoRecordEntity?>

    @Query("SELECT * FROM records_minijuegos ORDER BY timestamp DESC")
    fun getTodosLosRecords(): Flow<List<MinijuegoRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarRecord(record: MinijuegoRecordEntity)
}
