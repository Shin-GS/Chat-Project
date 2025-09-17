package com.chat.server.controller.hx.conversation.group;

import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.service.common.request.CustomPageRequest;
import com.chat.server.service.common.request.CustomPageRequestDefault;
import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static com.chat.server.common.constant.FragmentConstants.*;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/groups")
public class ConversationGroupSearchHxController {
    private final ConversationGroupService conversationGroupService;

    @Operation(summary = "그룹 프로필 모달")
    @GetMapping("/{conversationId}/profile/modal")
    public List<ModelAndView> friendProfile(@PathVariable("conversationId") ConversationId conversationId,
                                            @JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_GROUP_PROFILE_MODAL_PATH,
                        CONVERSATION_GROUP_PROFILE_MODAL_FRAGMENT,
                        Map.of(CONVERSATION_GROUP_PROFILE_MODAL_CONVERSATION_GROUP_INFO, conversationGroupService.getGroupProfile(conversationId),
                                CONVERSATION_GROUP_PROFILE_MODAL_IS_JOINED, conversationGroupService.isJoined(conversationId, memberInfo.id())))
                .build();
    }

    @Operation(summary = "그룹 대화방 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> searchGroupModal() {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_GROUP_SEARCH_MODAL_PATH,
                        CONVERSATION_GROUP_SEARCH_MODAL_FRAGMENT)
                .build();
    }

    @Operation(summary = "그룹 대화방 검색")
    @GetMapping("/search")
    public List<ModelAndView> searchGroup(@RequestParam("keyword") String keyword,
                                          @CustomPageRequestDefault(limit = 5, maxLimit = 50) CustomPageRequest pageRequest,
                                          @JwtMember JwtMemberInfo memberInfo) {
        CustomPageResponse<ConversationInfoResponse> customPageResponse = conversationGroupService.findConversations(memberInfo.id(), keyword, pageRequest.toPageable());
        String hrefBase = UriComponentsBuilder
                .fromPath("/hx/conversations/groups/search")
                .queryParam("keyword", keyword)
                .queryParam("limit", pageRequest.limit())
                .build()
                .toUriString();

        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_GROUP_SEARCH_RESULT_PATH,
                        CONVERSATION_GROUP_SEARCH_RESULT_FRAGMENT,
                        Map.of(CONVERSATION_GROUP_SEARCH_RESULT_CONVERSATION_LIST, customPageResponse.content(),
                                CONVERSATION_GROUP_SEARCH_RESULT_HREF_BASE, hrefBase,
                                CONVERSATION_GROUP_SEARCH_RESULT_PAGE, customPageResponse))
                .build();
    }
}
