package com.superapp.booking_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

@Configuration
public class RedisScriptConfig {

    @Bean
    public DefaultRedisScript<Long> joinScript() {
        var script = new DefaultRedisScript<Long>();
        script.setLocation(new ClassPathResource("scripts/q_join.lua"));
        script.setResultType(Long.class); // returns position
        return script;
    }

    @Bean
    public DefaultRedisScript<List> promoteScript() {
        var script = new DefaultRedisScript<List>();
        script.setLocation(new ClassPathResource("scripts/q_promote.lua"));
        script.setResultType(List.class); // returns list of userIds
        return script;
    }

    @Bean
    public DefaultRedisScript<List> claimScript() {
        var script = new DefaultRedisScript<List>();
        script.setLocation(new ClassPathResource("scripts/q_claim.lua"));
        script.setResultType(List.class); // returns ['ok'] or ['err', reason]
        return script;
    }
}
