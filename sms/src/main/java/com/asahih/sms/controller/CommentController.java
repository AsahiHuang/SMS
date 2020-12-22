package com.asahih.sms.controller;

import com.asahih.sms.pojo.Comment;
import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.Post;
import com.asahih.sms.pojo.User;
import com.asahih.sms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Asahi Huang on 2020/12/11.
 */
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    LogService logService;
    @Autowired
    JavaMailSender mailSender;

    // 返回主题的所有评论
    @GetMapping("/api/posts/{pid}/comments")
    @CrossOrigin
    public List<Comment> listByPostId(@PathVariable("pid") int pid) throws Exception{
        return commentService.listByPost(pid);
    }

    // 返回回复的历史评论
    // 这里使用DFS算法进行遍历
    @GetMapping("/api/comments/{reviewId}/history")
    @CrossOrigin
    public List<Comment> listCommentHistory(@PathVariable("reviewId") int reviewId) throws Exception{
        Stack<Comment> stack = new Stack<>();
        List<Comment> history = new ArrayList<>();
        Comment top = commentService.findCommentById(reviewId);
        // 先找到开端
        while(top.getReviewId() != 0)
            top = commentService.findCommentById(top.getReviewId());
        // 找到最开端后将top压入栈
        stack.push(top);
        // DFS 搜索所有top分支下的评论
        while(!stack.isEmpty()){
            top = stack.pop();
            history.add(top);
            List<Comment> children = commentService.findAllByReviewId(top.getId());
            if (children != null && children.size() > 0)
                for (int i = children.size()-1; i >= 0; i--)
                    stack.push(children.get(i));
        }
        return history;
    }

    @GetMapping("/api/{username}/news")
    @CrossOrigin
    public List<Comment> listNewsByUsername(@PathVariable("username") String username) throws  Exception {
        return commentService.listByToUsername(username);
    }
    @GetMapping("/api/{username}/reviews")
    @CrossOrigin
    public List<Comment> listReviewByUsername(@PathVariable("username") String username) throws  Exception {
        return commentService.listByUsername(username);
    }



    @PostMapping("/api/posts/comments")
    @CrossOrigin
    public Comment addComment(@RequestBody Comment comment) throws Exception {
        commentService.addOrUpdate(comment);
        logService.addLog(new Log("多人-发布评论",comment.getUsername(),comment.getToUsername(), LocalDateTime.now().toString(),"评论成功","用户") );
        logService.addLog(new Log("多人-收到评论",comment.getToUsername(),comment.getUsername(), LocalDateTime.now().toString(),"接收成功","用户") );
        sendReviewMail(comment);
        return comment;
    }

    @PostMapping("/api/reviews/delete")
    @CrossOrigin
    public void deleteComment(@RequestBody Comment comment) throws Exception {
        commentService.deleteComment(comment.getId());
    }



    public void sendReviewMail(Comment comment) throws Exception {
        // 如果评论自己，就不需要提醒了
        if (comment.getToUsername().equals(comment.getUsername()))
            return;
        SimpleMailMessage message = new SimpleMailMessage();
        User user = userService.getUserByUsername(comment.getToUsername());
        Post post = postService.getPostById(comment.getPid());
        String owner = (comment.getToUsername().equals(post.getUser().getUsername())) ? "您" : post.getUser().getUsername();
        String text = "你好 " + user.getUsername() +
                ":\n\n在" + owner +  "发布的：[订阅社区]-[" + post.getTitle() + "] 收到新评论，内容：\n\n@"
                + comment.getUsername() + "："
                + comment.getSendText() + "\n\n"
                + "请前往网站：[订阅社区]-[评论]-[回复我的] 查看更多内容。\n\n订阅服务管理系统";
        message.setFrom("asahih@163.com");
        message.setTo(user.getEmail());
        message.setSubject("新回复提醒");
        message.setText(text);
        mailSender.send(message);
        logService.addLog(new Log("用户-评论提醒",comment.getUsername(), comment.getToUsername(),LocalDateTime.now().toString(),"提醒成功","系统"));
    }

}
