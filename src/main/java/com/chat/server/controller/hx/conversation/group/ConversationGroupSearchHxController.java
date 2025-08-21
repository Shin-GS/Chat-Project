package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.common.request.CustomPageRequest;
import com.chat.server.service.common.request.CustomPageRequestDefault;
import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/groups")
public class ConversationGroupSearchHxController {
    private final ConversationService conversationService;

    @Operation(summary = "그룹 채팅방 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> searchGroupModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/search/modal.html",
                        "components/conversation/group/search/modal :: search-modal")
                .build();
    }

    @Operation(summary = "그룹 채팅방 검색")
    @GetMapping("/search")
    public List<ModelAndView> searchGroup(@RequestParam("keyword") String keyword,
                                          @CustomPageRequestDefault(limit = 5, maxLimit = 50) CustomPageRequest pageRequest,
                                          @JwtMember JwtMemberInfo memberInfo) {
        CustomPageResponse<ConversationInfoResponse> customPageResponse = conversationService.findConversations(memberInfo.id(), keyword, pageRequest.toPageable());
        String hrefBase = UriComponentsBuilder
                .fromPath("/hx/conversations/groups/search")
                .queryParam("keyword", keyword)
                .queryParam("limit", pageRequest.limit())
                .build()
                .toUriString();

        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/group/search/result.html",
                        "components/conversation/group/search/result :: conversation-group-list",
                        Map.of("conversations", customPageResponse.content(),
                                "hrefBase", hrefBase,
                                "page", customPageResponse))
                .build();
    }
}
