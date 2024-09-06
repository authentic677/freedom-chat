package xyz677123.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz677123.mapper.PostMapper;
import xyz677123.pojo.Post;

@Service
public class PostService extends ServiceImpl<PostMapper, Post> {
}
