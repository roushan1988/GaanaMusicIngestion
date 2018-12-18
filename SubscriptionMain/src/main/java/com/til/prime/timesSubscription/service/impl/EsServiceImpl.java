package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dao.RawDataRepository;
import com.til.prime.timesSubscription.es.ESRepository;
import com.til.prime.timesSubscription.model.RawESEntity;
import com.til.prime.timesSubscription.model.RawMxGaanaDbEntity;
import com.til.prime.timesSubscription.pojo.EsQuerybuilder;
import com.til.prime.timesSubscription.pojo.SearchTuple;
import com.til.prime.timesSubscription.service.EsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class EsServiceImpl implements EsService {
    private static final Logger LOG = Logger.getLogger(EsServiceImpl.class);

    @Autowired
    private RawDataRepository dao;

    @Autowired
    private ESRepository esDao;

    @Override
//    @PostConstruct
    public void build() {
        int page = 0;
        loop1:
        while(true) {
            try {
                long start = System.currentTimeMillis();
                Page<RawMxGaanaDbEntity> pageResult = dao.findAll(new PageRequest(page++, 500000));
                if (pageResult==null || CollectionUtils.isEmpty(pageResult.getContent())) {
                    break loop1;
                }
                List<RawMxGaanaDbEntity> models = pageResult.getContent();
                List<RawESEntity> esModels = new ArrayList<>();
                models.parallelStream().forEach(i -> esModels.add(new RawESEntity(i.getTrackId(), getTitleRemoveSpecialCharacters(i.getTrackTitle()).toLowerCase(), i.getLanguage().toLowerCase())));
                esModels.removeAll(Collections.singleton(null));
                esDao.saveAll(esModels);
                Long timetaken = System.currentTimeMillis()-start;
                LOG.info("Done with page: "+page+" in millis: "+timetaken);
            }catch (Exception e){
                LOG.error("Exception", e);
            }
        }
        LOG.info("Completed indexing build");
    }

//    @PostConstruct
    public void test(){
        List<SearchTuple> queries = Arrays.asList(
                new SearchTuple("Kannada", "Purra Purra"),
                new SearchTuple("Bhojpuri", "Piyawa Laile Na Kanwariya"),
                new SearchTuple("Bhojpuri", "Mann Karata Sejiya Par Ladi"),
                new SearchTuple("Bhojpuri", "Maugi Gorki Chahi Na"),
                new SearchTuple("Bhojpuri", "Gaswa La da Raja Ji"),
                new SearchTuple("Bhojpuri", "Nache Kanwariya Jhum Jhum"),
                new SearchTuple("Bhojpuri", "Kanwariya Jhum"),
                new SearchTuple("Bhojpuri", "Leke Kanwariya")
        );
        for(SearchTuple tuple: queries){
            System.out.println("======================================");
            System.out.println(tuple);
            List<RawMxGaanaDbEntity> result = search(tuple);
            if(result==null){
                System.out.println(result);
            }else{
                for(RawMxGaanaDbEntity entity: result){
                    System.out.println(entity);
                }
            }
            System.out.println("--------------------------------------");
        }
    }

    @Override
    public List<RawMxGaanaDbEntity> search(SearchTuple tuple){
        Page<RawESEntity> esEntities = esDao.search(EsQuerybuilder.build(tuple.getLanguage(), getTitleRemoveSpecialCharacters(tuple.getTitle())), new PageRequest(0, 4));
        List<RawMxGaanaDbEntity> models = new ArrayList<>(esEntities.getContent().size());
        for(int i=0; i<4; i++){
            models.add(null);
        }
        if(esEntities!=null && CollectionUtils.isNotEmpty(esEntities.getContent())){
            List<RawESEntity> list = esEntities.getContent();
            list.stream().forEach(i -> {
                models.set(list.indexOf(i), dao.findByTrackId(i.getTrackId()));
            });
            LOG.info("Search done for tuple: "+tuple);
        }
        return models;
    }

    public static void main(String[] args) {
        SearchTuple tuple = new SearchTuple("Kannada", "Purra Purra");
        System.out.println(getTitleRemoveSpecialCharacters(tuple.getTitle()));
    }

    private static String getTitleRemoveSpecialCharacters(String title){
        return title.replaceAll(" +", " ").trim();
//        return title.replaceAll("[^a-zA-Z0-9]", " ").replaceAll(" +", " ").trim();
    }
}
