package zero.one.home3

enum class ErrorCode(val code: Int) {
    USER_NOT_FOUND(404),
    USERNAME_ALREADY_EXISTS(404),
}

enum class UserRole{
    ADMIN, USER
}