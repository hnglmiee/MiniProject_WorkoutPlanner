package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Request.UserInBodyRequest;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.UserInbodyResponse;
import com.workoutplanner.MiniProject.Services.Interfaces.IUserInBodyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.UserInBody.ROOT)
public class UserInBodyController {
    private final IUserInBodyService userInBodyService;

    public UserInBodyController(IUserInBodyService userInBodyService) {
        this.userInBodyService = userInBodyService;
    }

    @GetMapping("/my-in-body")
    public ApiResponse<List<UserInbodyResponse>> getMyInBody() {
        ApiResponse<List<UserInbodyResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userInBodyService.getMyInBody());
        apiResponse.setMessage("Get data successfully!");
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<UserInbodyResponse> createUserInBody(@RequestBody UserInBodyRequest request) {
        ApiResponse<UserInbodyResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userInBodyService.createUserInBody(request));
        apiResponse.setMessage("Create data successfully!");
        return apiResponse;
    }
}
