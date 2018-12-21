package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.GaanaDataRepository;
import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import com.til.prime.timesSubscription.service.ExecutorService;
import com.til.prime.timesSubscription.service.GaanaValidationService;
import com.til.prime.timesSubscription.util.HttpConnectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GaanaValidationServiceImpl implements GaanaValidationService {

    private static final Logger LOG = Logger.getLogger(GaanaValidationServiceImpl.class);

    @Autowired
    private HttpConnectionUtils httpConnectionUtils;

    @Autowired
    private GaanaDataRepository gaanaDao;

    @Autowired
    private ExecutorService executorService;

    @Override
//    @PostConstruct
    public void process(){
        int page = 0;
        loop1:
        while(true) {
            try {
                long start = System.currentTimeMillis();
                LOG.info("Starting with this batch");
                List<MxGaanaDbEntity> models = gaanaDao.findByJobTagAndYoutubeIdNotNull("FINAL_URL_LIST_FROM_GAABA");
                if (CollectionUtils.isEmpty(models)) {
                    break loop1;
                }
                int size=20;
                for(int i=0; i<models.size(); i+=size){
                    int end = Math.min(i + size, models.size());
                    List<MxGaanaDbEntity> sublist = models.subList(i, end);
                    executorService.getExecutorService().submit(new ValidationTask(sublist, httpConnectionUtils, gaanaDao));
                }
                break loop1;
            }catch (Exception e){
                LOG.error("Exception", e);
            }
        }
    }

    private static final List<String> keys = Arrays.asList(
            "AIzaSyARh-cwvt1c2PnEBLmFHlRs6hvVaTetUfE",
            "AIzaSyAZeoIX4JlyCCQX1gorPjch1Z1EKsga2dA",
            "AIzaSyDxjjdtx153deJMa5a9rQHd-nYk_9emAG0",
            "AIzaSyB5-n6BnZh0k1RqEtlG6oJ3kUu0rFiQnkI",
            "AIzaSyDCuZCy7jzN5eqGz9K0IbHjpi6Ve9UrJ_4",
            "AIzaSyABOXjAkQgwHHVTyPJp-6nm_4TR7L9-ApQ",
            "AIzaSyAtca1qXkDEE2-_d4SH0XaMLmqaI6mwHmM",
            "AIzaSyDkRD7vZsLFLsYks0zz4ObrqeTCFJcsshc",
            "AIzaSyC9IqjuBB1DrA6Kw5qA28yGAlxfjeiHR24");

    private static final Random rand = new Random();
    public static String getRandomKey(){
        int randomIndex = rand.nextInt(keys.size());
        String randomElement = keys.get(randomIndex);
        return randomElement;
    }

    static class YTVideoResponse{
        private List<YTVideoResponseItem> items;

        public List<YTVideoResponseItem> getItems() {
            return items;
        }

        public void setItems(List<YTVideoResponseItem> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTVideoResponse{");
            sb.append("items=").append(items);
            sb.append('}');
            return sb.toString();
        }
    }

    static class YTVideoStatistics {
        private Long viewCount;
        private Long likeCount;
        private Long dislikeCount;
        private Long favouriteCount;
        private Long commentCount;

        public Long getViewCount() {
            return viewCount;
        }

        public void setViewCount(Long viewCount) {
            this.viewCount = viewCount;
        }

        public Long getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Long likeCount) {
            this.likeCount = likeCount;
        }

        public Long getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(Long dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public Long getFavouriteCount() {
            return favouriteCount;
        }

        public void setFavouriteCount(Long favouriteCount) {
            this.favouriteCount = favouriteCount;
        }

        public Long getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Long commentCount) {
            this.commentCount = commentCount;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTVideoStatistics{");
            sb.append("viewCount=").append(viewCount);
            sb.append(", likeCount=").append(likeCount);
            sb.append(", dislikeCount=").append(dislikeCount);
            sb.append(", favouriteCount=").append(favouriteCount);
            sb.append(", commentCount=").append(commentCount);
            sb.append('}');
            return sb.toString();
        }
    }

    static class YTVideoResponseItem{
        private String id;
        private YTVideoStatistics statistics;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public YTVideoStatistics getStatistics() {
            return statistics;
        }

        public void setStatistics(YTVideoStatistics statistics) {
            this.statistics = statistics;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTVideoResponseItem{");
            sb.append("id='").append(id).append('\'');
            sb.append(", statistics=").append(statistics);
            sb.append('}');
            return sb.toString();
        }
    }

    static class ValidationTask implements Runnable {
        private List<MxGaanaDbEntity> modelList;
        private HttpConnectionUtils httpConnectionUtils;
        private GaanaDataRepository gaanaDao;

        public ValidationTask(List<MxGaanaDbEntity> modelList, HttpConnectionUtils httpConnectionUtils, GaanaDataRepository gaanaDao) {
            this.modelList = modelList;
            this.httpConnectionUtils = httpConnectionUtils;
            this.gaanaDao = gaanaDao;
        }

        @Override
        public void run() {
            for(MxGaanaDbEntity entity: modelList){
                try {
                    String videoId= entity.getYoutubeId().replaceAll("https://www.youtube.com/watch\\?v=", "");
                    YTVideoResponse response = fetchVideoStats(videoId);
                    LOG.info("videoId: "+ videoId+ ", Response: "+response);
                    if (CollectionUtils.isNotEmpty(response.getItems())) {
                        entity.setValid(true);
                    } else {
                        entity.setValid(false);
                    }
                }catch (Exception e){
                    LOG.error("Exception", e);
                    entity.setValid(false);
                }
            }
            gaanaDao.saveAll(modelList);
        }

        private String createVideoStatsFetchUrl(String videoId, String key){
            StringBuilder sb = new StringBuilder();
            sb.append("https://www.googleapis.com/youtube/v3/videos?part=id&type=video&id=").append(videoId).append("&key=").append(key);
            return sb.toString();
        }

        private YTVideoResponse fetchVideoStats(String videoId){
            int retryCount = GlobalConstants.API_RETRY_COUNT;
            RETRY_LOOP:
            while(retryCount>0){
                try{
                    YTVideoResponse response = httpConnectionUtils.requestForObject("", createVideoStatsFetchUrl(videoId, getRandomKey()), YTVideoResponse.class, GlobalConstants.GET);
                    return response;
                }catch (Exception e){
                    LOG.error("Exception", e);
                    retryCount--;
                    continue RETRY_LOOP;
                }
            }
            return null;
        }
    }

}
