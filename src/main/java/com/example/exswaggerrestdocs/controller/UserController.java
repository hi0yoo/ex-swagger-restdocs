package com.example.exswaggerrestdocs.controller;

import com.example.exswaggerrestdocs.common.ApiResponse;
import com.example.exswaggerrestdocs.common.MessageResponse;
import com.example.exswaggerrestdocs.common.ValidationException;
import com.example.exswaggerrestdocs.controller.request.UserAddRequest;
import com.example.exswaggerrestdocs.controller.request.UserModifyRequest;
import com.example.exswaggerrestdocs.controller.response.UserAddResponse;
import com.example.exswaggerrestdocs.controller.response.UserDetailsResponse;
import com.example.exswaggerrestdocs.entity.Gender;
import com.example.exswaggerrestdocs.entity.User;
import com.example.exswaggerrestdocs.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserAddResponse> userAdd(@Validated @RequestBody UserAddRequest request,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Long userId = userService.saveUser(
                new User(
                        request.getName(),
                        request.getAge(),
                        request.getGender()
                )
        );
        return ApiResponse.createSuccess(new UserAddResponse(userId));
    }

    @PatchMapping("/{userId}")
    public ApiResponse<MessageResponse> userModify(@PathVariable Long userId,
                                                   @RequestBody UserModifyRequest request) {
        userService.modifyName(userId, request.getName());
        return ApiResponse.createSuccess(new MessageResponse("유저 데이터 수정을 성공적으로 완료하였습니다."));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<MessageResponse> userDelete(@PathVariable Long userId) {
        userService.removeUser(userId);
        return ApiResponse.createSuccess(new MessageResponse("유저 데이터를 성공적으로 삭제하였습니다."));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserDetailsResponse> userDetails(@PathVariable Long userId) {
        User user = userService.findUserBy(userId);
        return ApiResponse.createSuccess(new UserDetailsResponse(user));
    }

    @GetMapping
    public ApiResponse<List<UserDetailsResponse>> userList(@RequestParam Gender gender) {
        if (gender == null) {
            List<UserDetailsResponse> userDetailsList = userService.findAllUser().stream()
                    .map(UserDetailsResponse::new)
                    .collect(Collectors.toList());
            return ApiResponse.createSuccess(userDetailsList);
        }

        List<UserDetailsResponse> userDetailsList = userService.findUserBy(gender).stream()
                .map(UserDetailsResponse::new)
                .collect(Collectors.toList());
        return ApiResponse.createSuccess(userDetailsList);
    }
}
