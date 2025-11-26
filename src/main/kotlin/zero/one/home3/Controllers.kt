package zero.one.home3

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/category")
class CategoryController(){



}

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService : UserService
){


    @Operation(summary = "Create user")
    @PostMapping
    fun create(@RequestBody userRequest: UserRequest) = userService.create(userRequest)
}