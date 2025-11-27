import java.util.Optional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException // Sizning xato sinfingizni almashtiring
import zero.one.home3.UserNotFoundException


fun <T> Optional<T>.getOrThrowNotFound(myThrow: Throwable): T {
    return this.orElseThrow {
        myThrow
    }
}