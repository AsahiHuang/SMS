package com.asahih.sms.controller;

import com.asahih.sms.pojo.*;
import com.asahih.sms.service.*;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/10/25.
 */

@RestController
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    LogService logService;
    @Autowired
    CollectService collectService;

    @GetMapping("/api/posts")
    @CrossOrigin
    public List<Post> list() throws Exception{
        return postService.list();
    }

    @GetMapping("/api/posts/{id}")
    @CrossOrigin
    public List<Post> viewPost(@PathVariable("id") int id) throws Exception{
        return  postService.listByIDAndAvailable(id);
    }


    @GetMapping("/api/posts/available")
    @CrossOrigin
    public List<Post> listAvailable() throws Exception{
        return postService.listAvailable();
    }

    @PostMapping("/api/posts")
    @CrossOrigin
    public Post addOrUpdate(@RequestBody Post post) throws Exception{
        User user = userService.getUserByUsername(post.getUser().getUsername());
        post.setUser(user);
        Category category = categoryService.get(post.getCategory().getId());
        post.setCategory(category);
        logService.addLog(new Log("多人-添加",post.getUser().getUsername(), post. getTitle(), LocalDateTime.now().toString(),"添加成功","用户"));
        postService.addOrUpdate(post);
        return post;
    }

    @GetMapping("/api/categories/{cid}/posts")
    @CrossOrigin
    public List<Post> listByCategory(@PathVariable("cid") int cid) throws Exception{
        if (0 != cid) {
            return postService.listByCategory(cid);
        } else {
            return postService.listAvailable();
        }
    }

    // 全部中搜索未隐藏的
    @CrossOrigin
    @GetMapping("/api/posts/search")
    @ResponseBody
    public List<Post> searchResult(@RequestParam("keywords") String keywords) {
        if ("".equals(keywords))
            return postService.listAvailable();
        else
            return postService.searchPost(keywords);

    }

    //用户帖中搜索
    @CrossOrigin
    @GetMapping("/api/{username}/posts/search")
    @ResponseBody
    public List<Post> searchResultWithUsername(@RequestParam("keywords") String keywords,@PathVariable("username") String username) throws Exception {
        if ("".equals(keywords))
            return postService.listByUsername(username);
        else{
            User user = userService.getUserByUsername(username);
            return postService.searchPostWithUser(user,keywords);
        }

    }

    // 用户收藏中搜索
    @CrossOrigin
    @GetMapping("/api/{username}/collected/posts/search")
    @ResponseBody
    public List<Post> searchResultWithCollected(@RequestParam("keywords") String keywords,@PathVariable("username") String username) throws Exception{
        if ("".equals(keywords))
            return postService.listPostByUserCollect(username);
        else{
            return postService.listPostByUserCollectAndPostLike(username,keywords);
        }
    }




    // 用户名模糊搜索
    @CrossOrigin
    @GetMapping("/api/posts/username/search")
    @ResponseBody
    public List<Post> searchResultWithUsernameLike(@RequestParam("keywords") String keywords) throws Exception {
        if ("".equals(keywords))
            return postService.list();
        else{
            return postService.searchPostWithUserLike(keywords);
        }
    }
    // 订阅名称模糊搜索
    @CrossOrigin
    @GetMapping("/api/posts/title/search")
    @ResponseBody
    public List<Post> searchResultWithTitle(@RequestParam("keywords") String keywords) {
        if ("".equals(keywords))
            return postService.list();
        else
            return postService.searchPostWithTitleLike(keywords);

    }

    @PostMapping("/api/posts/delete")
    @CrossOrigin
    public void deletePost(@RequestBody Post requestPost) throws Exception {
        Post post = postService.getPostById(requestPost.getId());
        String st = (requestPost.getUser().getUsername().equals(post.getUser().getUsername())) ? "用户" : ("管理员" + requestPost.getUser().getUsername());
        logService.addLog(new Log("多人-删除",post.getUser().getUsername(), post.getTitle(), LocalDateTime.now().toString(),"删除成功",st));
        collectService.deleteAllByPost(post);
        postService.deletePostById(post.getId());
    }

    // 管理中心显示全部
    @GetMapping("/api/{username}/posts")
    @CrossOrigin
    public List<Post> listPostByUser(@PathVariable("username") String username) throws Exception{
        return postService.listAllByUsername(username);
    }

    // 修改帖子状态（隐藏或显示）
    @PostMapping("/api/post/available")
    @CrossOrigin
    public Post changeStatus(@RequestBody Post requestPost) throws Exception {
        Post post = postService.getPostById(requestPost.getId());
        post.setAvailable(!post.isAvailable());
        postService.addOrUpdate(post);
        String logHandleSt = "修改为" + ((post.isAvailable())?"显示":"隐藏");
        logService.addLog(new Log("多人-修改状态",post.getUser().getUsername(), post.getTitle(), LocalDateTime.now().toString(),logHandleSt,"用户"));
        return post;
    }


}
