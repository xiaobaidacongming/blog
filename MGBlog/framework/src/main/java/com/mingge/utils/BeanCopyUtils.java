package com.mingge.utils;

import com.mingge.domain.entity.Article;
import com.mingge.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}

    /**
     * 拷贝Bean
     *  public static <V> 这个<>里是定义泛型的 只有定义了才能使用这个类型
     * @param source 源数据
     * @param clazz 字节码
     * @return 返回泛型Class<V>中的V
     */
    public static <V> V copyBean(Object source,Class<V> clazz){
        //创建目标对象
        V result = null;
        try {
            //用空参构造创建对象
            result = clazz.newInstance();
            //实现属性的拷贝，要求属性名保持一致
            BeanUtils.copyProperties(source,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,V> List<V> copyListBean(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("as");
        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
