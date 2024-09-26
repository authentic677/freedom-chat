package xyz677123.freedomchat.content_platform.conf;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.chat-bucket}")
    private String chatBucket;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        try {
            //如果不存在bucket则创建bucket
            boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(chatBucket).build());
            if (!b){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(chatBucket).build());
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        return minioClient;
    }
}
