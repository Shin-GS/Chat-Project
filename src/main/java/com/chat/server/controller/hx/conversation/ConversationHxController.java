package com.chat.server.controller.hx.conversation;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.request.ConversationCreateRequest;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations")
public class ConversationHxController {
    private final ConversationService conversationService;

    @Operation(summary = "내 대화방 목록 조회")
    @GetMapping
    public List<ModelAndView> myConversations(@JwtMember JwtMemberInfo memberInfo) {
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
                .build();
    }

    @Operation(summary = "대화방 생성")
    @PostMapping
    public List<ModelAndView> create(@ModelAttribute @Valid ConversationCreateRequest request,
                                     @JwtMember JwtMemberInfo memberInfo) {
        conversationService.create(memberInfo.id(), request.type(), request.userIds(), request.title());
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
                .build();
    }

    // todo 대화방 검색

    @Operation(summary = "1:1 대화방 입장")
    @PostMapping("/join/one-to-one/{friendUserId}")
    public List<ModelAndView> joinOneToOne(@PathVariable("friendUserId") Long friendUserId,
                                           @JwtMember JwtMemberInfo memberInfo) {
        conversationService.joinOneToOne(memberInfo.id(), friendUserId);
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
//                .addFragment("templates/components/common/modalClose.html",
//                        "components/common/modalClose :: close",
//                        "targetId",
//                        "search-conversation-group-list")
                .build();
    }

    @Operation(summary = "그룹 대화방 입장")
    @PostMapping("/join/group/{conversationId}")
    public List<ModelAndView> joinGroup(@PathVariable("conversationId") Long conversationId,
                                        @JwtMember JwtMemberInfo memberInfo) {
        conversationService.joinGroup(conversationId, memberInfo.id());
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
//                .addFragment("templates/components/common/modalClose.html",
//                        "components/common/modalClose :: close",
//                        "targetId",
//                        "search-friend-list")
                .build();
    }

    @Operation(summary = "대화방 나가기")
    @PostMapping("/{conversationId}/leave")
    public List<ModelAndView> leave(@PathVariable("conversationId") Long conversationId,
                                    @JwtMember JwtMemberInfo memberInfo) {
        conversationService.leave(conversationId, memberInfo.id());
        List<ConversationInfoResponse> conversations = conversationService.findConversations(memberInfo.id());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "request success"))
                .addFragment("templates/components/conversation/list.html",
                        "components/conversation/list :: conversation-list",
                        Map.of("conversations", conversations))
//                .addFragment("templates/components/common/modalClose.html",
//                        "components/common/modalClose :: close",
//                        "targetId",
//                        "search-friend-list")
                .build();
    }
}
