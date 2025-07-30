package br.com.EasyTable.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        // --- CORREÇÃO AQUI ---
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Use Jackson para valores
        // --- FIM DA CORREÇÃO ---

        // Opcional: Se você também quiser que os hash keys e hash values sejam serializados
        // template.setHashKeySerializer(new StringRedisSerializer());
        // template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet(); // Importante para que as configurações sejam aplicadas
        return template;
    }
}
