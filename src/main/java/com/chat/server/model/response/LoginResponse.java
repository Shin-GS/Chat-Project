package com.chat.server.model.response;

import com.chat.server.common.code.Code;

public record LoginResponse(Code code,
                            String token) {
}
