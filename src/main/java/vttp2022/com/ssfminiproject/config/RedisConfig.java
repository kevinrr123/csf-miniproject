package vttp2022.com.ssfminiproject.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//import vttp2022.com.ssfminiproject.models.User;

import java.util.Optional;

import org.slf4j.*;

@Configuration
public class RedisConfig {
  private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private Optional<Integer> redisPort;

  private String redisPassword = System.getenv("REDIS_PASSWORD");

  @Bean("repository")
  public RedisTemplate<String, Object> redisTemplate(){
    final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisHost);
    config.setPort(redisPort.get());
    config.setPassword(redisPassword);

    final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
    final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
    jedisFac.afterPropertiesSet();
    logger.info("redis host port > {redisHost} {redisPort}", redisHost, redisPort);

    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(jedisFac);
    template.setKeySerializer(new StringRedisSerializer());

    RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());
    template.setValueSerializer(
        serializer
    );
    
    return template;
  }
  
}
