package xyz677123.freedomchat.zone.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.pojo.User;

@FeignClient("freedom-chat-core")
public interface UserClient {
    @GetMapping("/api/user/{uid}")
    Result getUser(@PathVariable String uid);
}
