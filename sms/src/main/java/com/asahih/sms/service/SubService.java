package com.asahih.sms.service;

import com.asahih.sms.dao.SubDAO;
import com.asahih.sms.pojo.Category;
import com.asahih.sms.pojo.Sub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Asahi Huang on 2020/10/13.
 */
@Service
public class SubService {
    @Autowired
    SubDAO subDAO;
    @Autowired
    CategoryService categoryService;

    public List<Sub> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return subDAO.findAll(sort);
    }

    public void addOrUpdate(Sub sub) {
        subDAO.save(sub);
    }

    public void deleteById(int id) {
        subDAO.deleteById(id);
    }
    public void deleteByUsername(String username) {
        List<Sub> subs = listByUser(username);
        for (Sub sub : subs)
            deleteById(sub.getId());
    }


    // 无法区分用户
    public List<Sub> listByCategory(int cid) {
        Category category = categoryService.get(cid);
        return subDAO.findAllByCategory(category);
    }

    //用户单独查询显示
    public List<Sub> listByCategoryAndUser(int cid,String username) {
        Category category = categoryService.get(cid);
        return subDAO.findAllByCategoryAndUser(category,username);
    }

    public List<Sub> listByUser(String username){
        return  subDAO.findAllByUser(username);
    }

    public List<Sub> Search(String username,String keywords) {
        return subDAO.findAllByUserAndTitleLike(username, '%' + keywords + '%');
    }

}
