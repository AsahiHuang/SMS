package com.asahih.sms.service;

import com.asahih.sms.dao.PostDAO;
import com.asahih.sms.pojo.Category;
import com.asahih.sms.pojo.Collect;
import com.asahih.sms.pojo.Post;
import com.asahih.sms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/10/25.
 */
@Service
public class PostService {
    @Autowired
    PostDAO postDAO;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CollectService collectService;

    public Post getPostById(int id) {
        return postDAO.findById(id);
    }

    public List<Post> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return postDAO.findAll(sort);
    }
    public List<Post> listByIDAndAvailable(int id){
        return postDAO.findAllByIdAndAvailable(id,true);
    }

    public List<Post> listAvailable(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return postDAO.findAllByAvailable(true,sort);
    }

    public void addOrUpdate(Post post){
        postDAO.save(post);
    }


    public void deletePostById(int id){
        postDAO.deleteById(id);
    }
    public void deletePostByUsername(String username){
        List<Post> posts = listByUsername(username);
        for (Post post : posts)
            deletePostById(post.getId());
    }

    public List<Post> listByCategory(int cid){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Category category = categoryService.get(cid);
        return postDAO.findAllByCategoryAndAvailable(category,true,sort);
    }

    // 多人社区剔除隐藏的
    public List<Post> listByUsername(String username){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        User user = userService.getUserByUsername(username);
        return postDAO.findAllByUserAndAvailable(user,true,sort);
    }

    // 管理中心显示全部
    public List<Post> listAllByUsername(String username){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        User user = userService.getUserByUsername(username);
        return postDAO.findAllByUser(user,sort);
    }

    /*
        管理中心根据用户名模糊搜索

        先通过用户名模糊查找到所有用户对象，再根据得到到用户对象精确找到对应主题
     */
    public List<Post> searchPostWithUserLike(String keywords){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<Post> result = null;
        List<User> users = userService.searchUserByUsername("%" + keywords + "%");
        for (User user : users)
            if (result == null)
                result = (postDAO.findAllByUser(user,sort));
            else
                result.addAll(postDAO.findAllByUser(user,sort));

        return result;
    }
    public List<Post> searchPostWithTitleLike(String keywords){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return postDAO.findAllByTitleLike("%" + keywords + "%",sort);

    }
   /*
        订阅社区部分，仅显示未隐藏的帖
    */

    // 帖搜索（全部）
    public List<Post> searchPost(String keywords) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return postDAO.findAllByTitleLikeAndAvailable('%' + keywords + '%',true,sort);
    }
    // 帖搜索（仅自己）
    public List<Post> searchPostWithUser(User user,String keywords) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return postDAO.findAllByUserAndTitleLikeAndAvailable(user,'%' + keywords + '%',true,sort);
    }



    // 返回用户所有收藏帖
    public List<Post> listPostByUserCollect(String username) throws Exception{
        List<Collect> collects = collectService.listByUsernameAndCollected(username);
        List<Post> posts = new ArrayList<>();
        for (Collect collect : collects){
            Post post = collect.getPost();
            if (post.isAvailable())
                posts.add(collect.getPost());
        }
        return posts;
    }

    public List<Post> listPostByUserCollectAndPostLike(String username,String keywords) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<Post> likePosts = postDAO.findAllByTitleLikeAndAvailable('%' + keywords + '%',true,sort);
        List<Post> posts = new ArrayList<>();
        for (Post post : likePosts){
            Collect collect = collectService.listByUsernameAndCollectedAndPost(username,post);
            if (collect != null)
                posts.add(collect.getPost());
        }
        return posts;
    }




}
