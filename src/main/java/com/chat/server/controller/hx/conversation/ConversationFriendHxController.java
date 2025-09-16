package com.chat.server.controller.hx.conversation;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationFriendService;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import static com.chat.server.common.constant.FragmentConstants.*;

@Tag(name = "Conversation Page")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hx/conversations/friends")
public class ConversationFriendHxController {
    private final UserService userService;
    private final ConversationFriendService conversationFriendService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "친구 검색 모달")
    @GetMapping("/search/modal")
    public List<ModelAndView> searchSimilarUsernamesModal() {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_FRIEND_SEARCH_MODAL_PATH,
                        CONVERSATION_FRIEND_SEARCH_MODAL_FRAGMENT)
                .build();
    }

    @Operation(summary = "친구 검색")
    @GetMapping("/search")
    public List<ModelAndView> searchSimilarUsernames(@RequestParam("keyword") String keyword,
                                                     @JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_FRIEND_SEARCH_MODAL_RESULT_PATH,
                        CONVERSATION_FRIEND_SEARCH_MODAL_RESULT_FRAGMENT,
                        Map.of(CONVERSATION_FRIEND_SEARCH_MODAL_RESULT_USER_LIST, conversationFriendService.findFriendsByKeyword(keyword, memberInfo.id())))
                .build();
    }

    @Operation(summary = "내 친구 목록 조회")
    @GetMapping
    public List<ModelAndView> myFriends(@JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_FRIEND_LIST_PATH,
                        CONVERSATION_FRIEND_LIST_FRAGMENT,
                        Map.of(CONVERSATION_FRIEND_LIST_FRIEND_LIST, conversationFriendService.findFriends(memberInfo.id())))
                .build();
    }

    @Operation(summary = "친구 프로필 모달")
    @GetMapping("/{friendUserId}/profile/modal")
    public List<ModelAndView> friendProfile(@PathVariable("friendUserId") UserId friendUserId,
                                            @JwtMember JwtMemberInfo memberInfo) {
        return new ModelAndViewBuilder()
                .addFragment(CONVERSATION_FRIEND_PROFILE_MODAL_PATH,
                        CONVERSATION_FRIEND_PROFILE_MODAL_FRAGMENT,
                        Map.of(CONVERSATION_FRIEND_PROFILE_MODAL_MY_USER_ID, memberInfo.id(),
                                CONVERSATION_FRIEND_PROFILE_MODAL_FRIEND_USER_INFO, userService.getUserProfile(friendUserId),
                                CONVERSATION_FRIEND_PROFILE_MODAL_IS_FRIEND, userService.isFriend(memberInfo.id(), friendUserId)))
                .build();
    }

    @Operation(summary = "친구 추가")
    @PostMapping("/{friendUserId}")
    public List<ModelAndView> addFriends(@PathVariable("friendUserId") UserId friendUserId,
                                         @JwtMember JwtMemberInfo memberInfo) {
        conversationFriendService.addFriend(memberInfo.id(), friendUserId);
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.FRIEND_ADDED)))
                .addFragment(COMMON_MODAL_CLOSE_PATH,
                        COMMON_MODAL_CLOSE_FRAGMENT,
                        COMMON_MODAL_CLOSE_TARGET_ID,
                        COMMON_MODAL_CLOSE_TARGET_MODAL_CONTAINER)
                .addFragment(CONVERSATION_FRIEND_LIST_PATH,
                        CONVERSATION_FRIEND_LIST_FRAGMENT,
                        Map.of(CONVERSATION_FRIEND_LIST_FRIEND_LIST, conversationFriendService.findFriends(memberInfo.id())))
                .build();
    }

    @Operation(summary = "친구 삭제")
    @DeleteMapping("/{friendUserId}")
    public List<ModelAndView> removeFriends(@PathVariable("friendUserId") UserId friendUserId,
                                            @JwtMember JwtMemberInfo memberInfo) {
        conversationFriendService.removeFriend(memberInfo.id(), friendUserId);
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.FRIEND_DELETED)))
                .addFragment(COMMON_MODAL_CLOSE_PATH,
                        COMMON_MODAL_CLOSE_FRAGMENT,
                        COMMON_MODAL_CLOSE_TARGET_ID,
                        COMMON_MODAL_CLOSE_TARGET_MODAL_CONTAINER)
                .addFragment(CONVERSATION_FRIEND_LIST_PATH,
                        CONVERSATION_FRIEND_LIST_FRAGMENT,
                        Map.of(CONVERSATION_FRIEND_LIST_FRIEND_LIST, conversationFriendService.findFriends(memberInfo.id())))
                .addFragment(COMMON_MODAL_CLOSE_PATH,
                        COMMON_MODAL_CLOSE_FRAGMENT,
                        COMMON_MODAL_CLOSE_TARGET_ID,
                        COMMON_MODAL_CLOSE_TARGET_SEARCH_FRIEND_LIST)
                .build();
    }
}
