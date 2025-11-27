package zero.one.home3

enum class ErrorCode(val code: Int) {
    USER_NOT_FOUND(100),
    USERNAME_ALREADY_EXISTS(101),
    PRODUCT_NOT_FOUND(200),
    CATEGORY_NOT_FOUND(300),
    INSUFFICIENT_FUNDS_EXCEPTION(400)

}

enum class UserRole{
    ADMIN, USER
}