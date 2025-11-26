package zero.one.home3


import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
    fun findAllNotDeleted(pageable: Pageable): Page<T>
}

class BaseRepositoryImpl<T : BaseEntity>(
    entiInformation: JpaEntityInformation<T, Long>, entityManager: EntityManager
): SimpleJpaRepository<T, Long>(entiInformation, entityManager), BaseRepository<T> {
    override fun findByIdAndDeletedFalse(id: Long): T? = findByIdOrNull(id)?.run { if (deleted) null else this }

    override fun trash(id: Long): T? {
        TODO("Not yet implemented")
    }

    override fun trashList(ids: List<Long>): List<T?> {
        TODO("Not yet implemented")
    }

    override fun findAllNotDeleted(): List<T> {
        TODO("Not yet implemented")
    }

    override fun findAllNotDeleted(pageable: Pageable): Page<T> {
        TODO("Not yet implemented")
    }
}
@Repository
interface UserRepository : BaseRepository<User>{
    fun existsByUsername(username: String): Boolean

}