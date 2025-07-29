package br.com.EasyTable.Services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void salvarPedido(String chave, Object pedido, long ttlEmMinutos) {
        redisTemplate.opsForValue().set(chave, pedido, ttlEmMinutos, TimeUnit.MINUTES);
    }

    public Object buscarPedido(String chave) {
        return redisTemplate.opsForValue().get(chave);
    }

    public void deletarPedido(String chave) {
        redisTemplate.delete(chave);
    }
}