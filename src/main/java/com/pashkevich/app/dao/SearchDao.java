package com.pashkevich.app.dao;

import com.pashkevich.app.model.Blog;
import com.pashkevich.app.model.Page;
import com.pashkevich.app.model.Tag;
import com.pashkevich.app.model.User;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Vlad on 20.05.17.
 */
@Service
@Transactional
public class SearchDao {

    @PersistenceContext
    private EntityManager em;

//    public List<Tag> findTags(String search){
//        FullTextEntityManager fullTextEntityManager =
//                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(Tag.class).get();
//        org.apache.lucene.search.Query luceneQuery = qb
//                .keyword()
//                .onFields("tag")
//                .matching(search)
//                .createQuery();
//        javax.persistence.Query jpaQuery =
//                fullTextEntityManager.createFullTextQuery(luceneQuery, Tag.class);
//        List<Tag> result = jpaQuery.getResultList();
//        return result;
//    }
//
//    public List<Page> findPages(String search){
//        FullTextEntityManager fullTextEntityManager =
//                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(Page.class).get();
//        org.apache.lucene.search.Query luceneQuery = qb
//                .keyword()
//                .onFields("pageName")
//                .matching(search)
//                .createQuery();
//        javax.persistence.Query jpaQuery =
//                fullTextEntityManager.createFullTextQuery(luceneQuery, Page.class);
//        List<Page> result = jpaQuery.getResultList();
//        return result;
//    }
//
//    public List<Site> findSites(String search){
//        FullTextEntityManager fullTextEntityManager =
//                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(Site.class).get();
//        org.apache.lucene.search.Query luceneQuery = qb
//                .keyword()
//                .onFields("siteName")
//                .matching(search)
//                .createQuery();
//        javax.persistence.Query jpaQuery =
//                fullTextEntityManager.createFullTextQuery(luceneQuery, Site.class);
//        List<Site> result = jpaQuery.getResultList();
//        return result;
//    }
//
//    public List<Content> findContent(String search){
//        FullTextEntityManager fullTextEntityManager =
//                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(Content.class).get();
//        org.apache.lucene.search.Query luceneQuery = qb
//                .keyword()
//                .onFields("content")
//                .matching(search)
//                .createQuery();
//        javax.persistence.Query jpaQuery =
//                fullTextEntityManager.createFullTextQuery(luceneQuery, Content.class);
//        List<Content> result = jpaQuery.getResultList();
//        return result;
//    }

    public List<User> findUser(String search) {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query fullTextQuery = queryBuilder
                .keyword()
                .onField("username")
                .matching(search)
                .createQuery();
        List results = fullTextEm.createFullTextQuery(fullTextQuery).getResultList();
        return results;
    }

    public List<Blog> findBlog(String search) {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(Blog.class).get();
        Query fullTextQuery = queryBuilder
                .keyword()
                .onFields("blogName", "description")
                .matching(search)
                .createQuery();
        List results = fullTextEm.createFullTextQuery(fullTextQuery).getResultList();
        return results;
    }

    public List<Page> findPage(String search) {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(Page.class).get();
        Query fullTextQuery = queryBuilder
                .keyword()
                .onFields("pageName", "value")
                .matching(search)
                .createQuery();
        List results = fullTextEm.createFullTextQuery(fullTextQuery).getResultList();
        return results;
    }

    public List<Tag> findTag(String search) {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(Tag.class).get();
        Query fullTextQuery = queryBuilder
                .keyword()
                .onField("tag")
                .matching(search)
                .createQuery();
        List results = fullTextEm.createFullTextQuery(fullTextQuery).getResultList();
        return results;
    }


}
