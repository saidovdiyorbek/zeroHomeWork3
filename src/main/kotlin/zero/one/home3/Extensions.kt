import java.util.Optional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException // Sizning xato sinfingizni almashtiring
import zero.one.home3.UserNotFoundException


fun <T> Optional<T>.getOrThrowNotFound(entityName: String, id: Long): T {
    return this.orElseThrow {
        UserNotFoundException()
    }
}