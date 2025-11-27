package zero.one.home3

import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.Locale

@RestControllerAdvice
class ExceptionHandler(
    private val messageSource: MessageSource
) {

    @ExceptionHandler(ShopAppException::class)
    fun handleShopException(ex: ShopAppException): ResponseEntity<BaseMessage> {
        val locale = LocaleContextHolder.getLocale()
        val message = try {
            messageSource.getMessage(ex.errorType().toString(), null, locale)
        } catch (e: NoSuchMessageException) {
            ex.errorType().toString().replace("_", " ").lowercase()
        }

        return ResponseEntity
            .badRequest()
            .body(BaseMessage(ex.errorType().code, message))
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(e: Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.status(500)
            .body(mapOf("error" to "Server xatosi"))
    }
}

sealed class ShopAppException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    protected open fun getErrorMessageArguments(): Array<Any?>? = null
    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
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

class ProductNotFoundException() : ShopAppException(){
    override fun errorType() = ErrorCode.PRODUCT_NOT_FOUND
}

class CategoryNotFoundException() : ShopAppException(){
    override fun errorType() = ErrorCode.CATEGORY_NOT_FOUND

}

class InsufficientFundsException() : ShopAppException(){
    override fun errorType() = ErrorCode.INSUFFICIENT_FUNDS_EXCEPTION

}