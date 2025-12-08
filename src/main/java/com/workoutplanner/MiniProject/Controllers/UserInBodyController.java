package com.workoutplanner.MiniProject.Controllers;

import com.workoutplanner.MiniProject.Constants.ApiPaths;
import com.workoutplanner.MiniProject.Payload.Request.UserInBodyUpdateRequest;
import com.workoutplanner.MiniProject.Payload.Response.ApiResponse;
import com.workoutplanner.MiniProject.Payload.Response.UserInBodyUpdateResponse;
import com.workoutplanner.MiniProject.Payload.Response.UserInbodyResponse;
import com.workoutplanner.MiniProject.Services.Interfaces.IUserInBodyService;
import jakarta.validation.Valid;
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

    @PutMapping("/{id}")
    public ApiResponse<UserInBodyUpdateResponse> updateUserInBody(@PathVariable Integer id, @RequestBody @Valid UserInBodyUpdateRequest request) {
        ApiResponse<UserInBodyUpdateResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userInBodyService.updateUserInBody(id, request));
        apiResponse.setMessage("Update data successfully!");
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteUserInBody(@PathVariable Integer id) {
        userInBodyService.deleteUserInBody(id);
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(Boolean.TRUE);
        apiResponse.setMessage("Delete data successfully!");
        return apiResponse;
    }
}
