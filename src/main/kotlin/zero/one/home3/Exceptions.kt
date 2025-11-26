package zero.one.home3

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.Locale

@ControllerAdvice
class ExceptionHandler(
    private val errorMessageSource: ResourceBundleMessageSource,
){
    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(ex: Throwable): ResponseEntity<Any> {
        when(ex){
            is ShopAppException -> {
                return ResponseEntity
                    .badRequest()
                    .body(ex.getErrorMessage(errorMessageSource))
            }

            else -> {
                ex.printStackTrace()
                return ResponseEntity
                    .badRequest().body(
                        BaseMessage(100,
                            "Iltimos support bilan bog'laning")
                    )
            }
        }
    }
}

sealed class ShopAppException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    protected open fun getErrorMessageArguments(): Array<Any?>? = null
    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().code.toString(),
                getErrorMessageArguments() as Array<out Any>?,
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}

class UserNotFoundException() : ShopAppException(){
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class UserAlreadyExistsException() : ShopAppException(){
    override fun errorType() = ErrorCode.USERNAME_ALREADY_EXISTS
}