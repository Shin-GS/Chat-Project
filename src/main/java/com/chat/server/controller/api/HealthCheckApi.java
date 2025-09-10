package com.chat.server.controller.api;

import com.chat.server.common.response.CustomResponseBuilder;
import com.chat.server.common.response.Response;
import com.chat.server.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckApi {
    private final CustomResponseBuilder responseBuilder;

    @GetMapping("/api/health-check")
    public Response<Void> healthCheck() {
        return responseBuilder.of(SuccessCode.SUC);
    }
}
