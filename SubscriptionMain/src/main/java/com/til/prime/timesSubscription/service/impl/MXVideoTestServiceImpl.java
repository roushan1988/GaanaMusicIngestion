package com.til.prime.timesSubscription.service.impl;

import com.opencsv.CSVReader;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dao.GaanaDataRepository;
import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import com.til.prime.timesSubscription.model.YTSearchResultsEntity;
import com.til.prime.timesSubscription.service.ExecutorService;
import com.til.prime.timesSubscription.service.MXVideoTestService;
import com.til.prime.timesSubscription.util.HttpConnectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MXVideoTestServiceImpl implements MXVideoTestService {
    private static final Logger LOG = Logger.getLogger(MXVideoTestServiceImpl.class);

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

    @Autowired
    private HttpConnectionUtils httpConnectionUtils;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private GaanaDataRepository gaanaDao;

    private static int validCount=0;
    private static int invalidCount=0;

    @Override
    @PostConstruct
    public void test() throws Exception{
        populateURLs();
    }

    private void updateYTUrl(MxGaanaDbEntity model, List<YTVideoCrawlerResponseItem> items){
        try{
            List<YTSearchResultsEntity> results = new ArrayList<>();
            if(model.getSongs()==null){
                model.setSongs(new ArrayList<>());
            }
            if(CollectionUtils.isNotEmpty(items)) {
                for (YTVideoCrawlerResponseItem item : items) {
                    YTSearchResultsEntity result = new YTSearchResultsEntity();
                    result.setTitle(item.getTitle());
                    result.setUrl(item.getLink());
                    result.setChannel(item.getPublisher());
                    result.setViewCount(item.getViewCount());
                    result.setThumbnail(item.getImg_thumbnail());
                    result.setMaxResolutionThumbnail(item.getImg_max_resolution());
                    result.setTime(item.getTime());
                    result.setSong(model);
                    result.setCreated(new Date());
                    model.getSongs().add(result);
                    //                LOG.info(result.getViewCount());
                }
                boolean flag = false;
                loop:
                for (YTVideoCrawlerResponseItem item : items) {
                    if (item.getViewCount() != null && item.getViewCount() > 1000000 && StringUtils.isNotEmpty(item.getTime())) {
                        String length = item.getTime();
                        String[] times = length.split(":");
                        if (times.length > 2) {
                            continue loop;
                        }
                        if (times.length == 1) {
                            model.setYoutubeId(item.getLink());
                            model.setThumbnail(item.getImg_thumbnail());
                            model.setMaxResolutionThumbnail(item.getImg_max_resolution());
                            flag = true;
                            break loop;
                        }
                        if (times.length == 2) {
                            if (Integer.parseInt(times[0]) > 15) {
                                continue loop;
                            } else {
                                model.setYoutubeId(item.getLink());
                                model.setThumbnail(item.getImg_thumbnail());
                                model.setMaxResolutionThumbnail(item.getImg_max_resolution());
                                flag = true;
                                break loop;
                            }
                        }
                    }
                }
                if (!flag) {
                    model.setYoutubeId("NA");
                    model.setThumbnail("NA");
                    model.setMaxResolutionThumbnail("NA");
                }
            }
        }catch (Exception e){
            LOG.error("Exception", e);
        }
    }

    private LinkedHashMap<String, List<YTVideoCrawlerResponseItem>> searchVideosNew(List<String> titles){
        String q = StringUtils.join(titles, ",");
        LOG.info("searching for: "+ q);
        int retryCount = GlobalConstants.API_RETRY_COUNT;
        RETRY_LOOP:
        while(retryCount>0){
            try{
                LinkedHashMap<String, List<YTVideoCrawlerResponseItem>> response = (LinkedHashMap<String, List<YTVideoCrawlerResponseItem>>)httpConnectionUtils.requestForObject("", createVideoSearchUrl(q), Map.class, GlobalConstants.GET);
                LOG.info("Response: "+response);
                return response;
            }catch (Exception e){
                LOG.error("Exception", e);
                retryCount--;
                continue RETRY_LOOP;
            }
        }
        return null;
    }

    private void updateViews(YTVideoCrawlerResponseItem item){
        if(item!=null) {
            try {
                String views = item.getViews();
                views = views.replaceAll(" views", "");
                views = views.replaceAll(",", "");
                if (views.endsWith("B")) {
                    views = views.replaceAll("B", "");
                    Double d = Double.parseDouble(views.trim());
                    d = d * 1000000000d;
                    item.setViewCount(d.longValue());
                } else if (views.endsWith("M")) {
                    views = views.replaceAll("M", "");
                    Double d = Double.parseDouble(views.trim());
                    d = d * 1000000d;
                    item.setViewCount(d.longValue());
                } else if (views.endsWith("K")) {
                    views = views.replaceAll("K", "");
                    Double d = Double.parseDouble(views.trim());
                    d = d * 1000d;
                    item.setViewCount(d.longValue());
                }
            } catch (Exception e) {
                LOG.error("Exception", e);
            }
        }
    }

    private String createVideoStatsFetchUrl(String videoId, String key){
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/youtube/v3/videos?part=statistics&type=video&id=").append(videoId).append("&key=").append(key);
        return sb.toString();
    }

    private String createVideoSearchUrl(String query){
        StringBuilder sb = new StringBuilder();
        sb.append("http://localhost:3000?q=").append(query);
        return sb.toString();
    }

    private void populateURLs() throws Exception{
        int page = 0;
        loop1:
        while(true) {
            try {
                List<MxGaanaDbEntity> models = gaanaDao.findFirst10ByYoutubeIdNullOrderByPopularityIndexDesc();
                if (CollectionUtils.isEmpty(models)) {
                    break loop1;
                }
                List<String> qList = models.stream().map(i -> i.getTrackTitle().replaceAll(" ", "+").replaceAll(",", "+").replaceAll("\\?", "+")).collect(Collectors.toList());
                List<Long> idList = models.stream().map(MxGaanaDbEntity::getId).collect(Collectors.toList());
                LinkedHashMap<String, List<YTVideoCrawlerResponseItem>> response = searchVideosNew(qList);
                Map<Long, List<YTVideoCrawlerResponseItem>> map = new HashMap<>();
                for(Long id: idList){
                    map.put(id, new ArrayList<>());
                }
                if (MapUtils.isNotEmpty(response)) {
                    List<List<YTVideoCrawlerResponseItem>> resList = new ArrayList<>(response.values());
                    for (int i=0; i<resList.size(); i++) {
                        for (Object item : resList.get(i)) {
                            if (item instanceof YTVideoCrawlerResponseItem) {
                                updateViews((YTVideoCrawlerResponseItem) item);
                                map.get(idList.get(i)).add((YTVideoCrawlerResponseItem) item);
                            }
                            if (item instanceof LinkedHashMap) {
                                YTVideoCrawlerResponseItem item1 = new YTVideoCrawlerResponseItem();
                                item1.setLink((String) ((LinkedHashMap) item).get("link"));
                                item1.setTitle((String) ((LinkedHashMap) item).get("title"));
                                item1.setPublisher((String) ((LinkedHashMap) item).get("publisher"));
                                item1.setViews((String) ((LinkedHashMap) item).get("views"));
                                item1.setTime((String) ((LinkedHashMap) item).get("time"));
                                item1.setImg_thumbnail((String) ((LinkedHashMap) item).get("img_thumbnail"));
                                item1.setImg_max_resolution((String) ((LinkedHashMap) item).get("img_max_resolution"));
                                updateViews(item1);
                                map.get(idList.get(i)).add(item1);
                            }
                        }
                    }
                }
                for (MxGaanaDbEntity model : models) {
                    updateYTUrl(model, map.get(model.getId()));
                }
                gaanaDao.saveAll(models);
                for (MxGaanaDbEntity model : models) {
                    System.out.println("===============================================");
                    LOG.info("track: " + model.getTrackTitle() + ", url: " + model.getYoutubeId());
                    System.out.println("===============================================");
                }
            }catch (Exception e){
                LOG.error("Exception in batch: ", e);
            }
//            Thread.sleep(5000);

//            List<MxGaanaDbEntity> list = new ArrayList<>();
//            for (MxGaanaDbEntity model : models) {
//                list.add(model);
//                if(list.size()==10){
//                    executorService.getExecutorService().submit(new UrlPopulationTask(list, httpConnectionUtils, gaanaDao));
//                }
//                list = new ArrayList<>();
//            }
//            if(CollectionUtils.isNotEmpty(list)){
//                executorService.getExecutorService().submit(new UrlPopulationTask(list, httpConnectionUtils, gaanaDao));
//            }
        }
    }

    static class UrlPopulationTask implements Runnable {
        private List<MxGaanaDbEntity> modelList;
        private HttpConnectionUtils httpConnectionUtils;
        private GaanaDataRepository gaanaDao;

        public UrlPopulationTask(List<MxGaanaDbEntity> modelList, HttpConnectionUtils httpConnectionUtils, GaanaDataRepository gaanaDao) {
            this.modelList = modelList;
            this.httpConnectionUtils = httpConnectionUtils;
            this.gaanaDao = gaanaDao;
        }

        private void updateYTUrl(MxGaanaDbEntity model, List<YTVideoCrawlerResponseItem> items){
            List<YTSearchResultsEntity> results = new ArrayList<>();
            if(model.getSongs()==null){
                model.setSongs(new ArrayList<>());
            }
            if(CollectionUtils.isNotEmpty(items)){
                model.setYoutubeId(items.get(0).getLink());
                for(YTVideoCrawlerResponseItem item: items){
                    YTSearchResultsEntity result = new YTSearchResultsEntity();
                    result.setTitle(item.getTitle());
                    result.setUrl(item.getLink());
                    result.setChannel(item.getPublisher());
                    result.setViewCount(item.getViewCount());
                    result.setSong(model);
                    model.getSongs().add(result);
                }
            }
        }

        @Override
        public void run() {
            try{
                Map<String, List<YTVideoCrawlerResponseItem>> response = searchVideosNew(modelList.stream().map(MxGaanaDbEntity::getTrackTitle).collect(Collectors.toList()));
                if(MapUtils.isNotEmpty(response)){
                    for(String key: response.keySet()){
                        if(CollectionUtils.isNotEmpty(response.get(key))){
                            for(YTVideoCrawlerResponseItem item: response.get(key)){
                                updateLikes(item);
                            }
                        }
                    }
                }
                for(MxGaanaDbEntity model: modelList){
                    updateYTUrl(model, response.get(model.getTrackTitle()));
                }
                gaanaDao.saveAll(modelList);
                for(MxGaanaDbEntity model: modelList){
                    System.out.println("===============================================");
                    LOG.info("track: "+ model.getTrackTitle()+", url: "+ model.getYoutubeId());
                    System.out.println("===============================================");
                }
            }catch (Exception e){
                LOG.error("Exception", e);
            }
        }

        private Map<String, List<YTVideoCrawlerResponseItem>> searchVideosNew(List<String> titles){
            int retryCount = GlobalConstants.API_RETRY_COUNT;
            RETRY_LOOP:
            while(retryCount>0){
                try{
                    Map<String, List<YTVideoCrawlerResponseItem>> response = (Map<String, List<YTVideoCrawlerResponseItem>>)httpConnectionUtils.requestForObject("", createVideoSearchUrl(StringUtils.join(titles, ","), null, null), Map.class, GlobalConstants.GET);
                    return response;
                }catch (Exception e){
                    LOG.error("Exception", e);
                    retryCount--;
                    continue RETRY_LOOP;
                }
            }
            return null;
        }

        private void updateLikes(YTVideoCrawlerResponseItem item){
            String views = item.getViews();
            Long count = 0l;
            views = views.replaceAll(",", "");
            if(views.endsWith("B")){
                views = views.replaceAll("B", "");
                Double d = Double.parseDouble(views.trim());
                d = d * 1000000000d;
                item.setViewCount(d.longValue());
            }else if(views.endsWith("M")){
                views = views.replaceAll("M", "");
                Double d = Double.parseDouble(views.trim());
                d = d * 1000000d;
                item.setViewCount(d.longValue());
            }else if(views.endsWith("K")){
                views = views.replaceAll("K", "");
                Double d = Double.parseDouble(views.trim());
                d = d * 1000d;
                item.setViewCount(d.longValue());
            }
        }

        private String createVideoStatsFetchUrl(String videoId, String key){
            StringBuilder sb = new StringBuilder();
            sb.append("https://www.googleapis.com/youtube/v3/videos?part=statistics&type=video&id=").append(videoId).append("&key=").append(key);
            return sb.toString();
        }

        private String createVideoSearchUrl(String title, String singer, String key){
            StringBuilder sb = new StringBuilder();
            String searchQuery = new StringBuilder(title).append(" ").append(singer).append(" full video song").toString();
            searchQuery = searchQuery.replaceAll(" ", "+").replaceAll(",","+");
            sb.append("https://www.googleapis.com/youtube/v3/search?part=id,snippet&order=relevance&type=video&maxResults=10&key=").append(key).append("&q=").append(searchQuery);
            return sb.toString();
        }
    }


//    private YTVideoResponse fetchVideoStats(String videoId){
//        int retryCount = GlobalConstants.API_RETRY_COUNT;
//        RETRY_LOOP:
//        while(retryCount>0){
//            try{
//                YTVideoResponse response = httpConnectionUtils.requestForObject("", createVideoStatsFetchUrl(videoId, getRandomKey()), YTVideoResponse.class, GlobalConstants.GET);
//                return response;
//            }catch (Exception e){
//                LOG.error("Exception", e);
//                retryCount--;
//                continue RETRY_LOOP;
//            }
//        }
//        return null;
//    }

//    private YTSearchResponse searchVideos(String title, String singer){
//        int retryCount = GlobalConstants.API_RETRY_COUNT;
//        RETRY_LOOP:
//        while(retryCount>0){
//            try{
//                YTSearchResponse response = httpConnectionUtils.requestForObject("", createVideoSearchUrl(title, singer, getRandomKey()), YTSearchResponse.class, GlobalConstants.GET);
//                return response;
//            }catch (Exception e){
//                LOG.error("Exception", e);
//                retryCount--;
//                continue RETRY_LOOP;
//            }
//        }
//        return null;
//    }



    static class YTUrlUpdate implements Runnable{
        private GaanaDataRepository gaanaDao;
        private String[] record;

        public YTUrlUpdate(GaanaDataRepository gaanaDao, String[] record) {
            this.gaanaDao = gaanaDao;
            this.record = record;
        }

        @Override
        public void run() {
            int retryCount = GlobalConstants.API_RETRY_COUNT;
            RETRY_LOOP:
            while(retryCount>0){
                try{
                    MxGaanaDbEntity entity = gaanaDao.findByTrackId(Long.parseLong(record[0]));
                    entity.setYoutubeId(record[2]);
                    gaanaDao.save(entity);
                    System.out.println("trackId: "+record[0]+", url: "+record[2]);
                    return;
                }catch (Exception e){
                    retryCount--;
                    continue RETRY_LOOP;
                }
            }
        }
    }


    private void populateYTUrlsFromCSV(){
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("/Users/roushan.singh1/MX/MxGaanaIntegration/gaana_min_filtered_with_youtube.csv");

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord = csvReader.readNext();

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                executorService.getExecutorService().submit(new YTUrlUpdate(gaanaDao, nextRecord));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void populateUrls() throws Exception{
//        int page = 0;
//        loop1:
//        while(true) {
//            List<MxGaanaDbEntity> models = gaanaDao.findAllByOrderByPopularityDescIndex(new PageRequest(page++, 1000));
//            if(CollectionUtils.isEmpty(models)){
//                break loop1;
//            }
//            for (MxGaanaDbEntity model : models) {
//                executorService.getExecutorService().submit(new GaanaValidationTask(model, httpConnectionUtils, gaanaDao));
//            }
//        }
//    }
//


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

    static class YTVideoIdentifier{
        private String videoId;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTVideoIdentifier{");
            sb.append("videoId='").append(videoId).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    static class YTSearchItem{
        private YTVideoIdentifier id;

        public YTVideoIdentifier getId() {
            return id;
        }

        public void setId(YTVideoIdentifier id) {
            this.id = id;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTSearchItem{");
            sb.append("id=").append(id);
            sb.append('}');
            return sb.toString();
        }
    }

    static class YTSearchResponse{
        private List<YTSearchItem> items;

        public List<YTSearchItem> getItems() {
            return items;
        }

        public void setItems(List<YTSearchItem> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTSearchResponse{");
            sb.append("items=").append(items);
            sb.append('}');
            return sb.toString();
        }
    }

    static class YTVideoCrawlerResponse{
        private List<YTVideoCrawlerResponseItem> items;

        public List<YTVideoCrawlerResponseItem> getItems() {
            return items;
        }

        public void setItems(List<YTVideoCrawlerResponseItem> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTVideoCrawlerResponse{");
            sb.append("items=").append(items);
            sb.append('}');
            return sb.toString();
        }
    }

    static class YTVideoResponse{
        private Map<String, List<YTVideoResponseItem>> items;

        public Map<String, List<YTVideoResponseItem>> getItems() {
            return items;
        }

        public void setItems(Map<String, List<YTVideoResponseItem>> items) {
            this.items = items;
        }
    }

    static class YTVideoCrawlerResponseItem{
        private String link;
        private String title;
        private String views;
        private Long viewCount;
        private Long likeCount;
        private String publisher;
        private boolean verified;
        private String time;
        private String img_thumbnail;
        private String img_max_resolution;


        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

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

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImg_thumbnail() {
            return img_thumbnail;
        }

        public void setImg_thumbnail(String img_thumbnail) {
            this.img_thumbnail = img_thumbnail;
        }

        public String getImg_max_resolution() {
            return img_max_resolution;
        }

        public void setImg_max_resolution(String img_max_resolution) {
            this.img_max_resolution = img_max_resolution;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("YTVideoCrawlerResponseItem{");
            sb.append("link='").append(link).append('\'');
            sb.append(", title='").append(title).append('\'');
            sb.append(", views='").append(views).append('\'');
            sb.append(", viewCount=").append(viewCount);
            sb.append(", likeCount=").append(likeCount);
            sb.append(", publisher='").append(publisher).append('\'');
            sb.append(", verified=").append(verified);
            sb.append(", time='").append(time).append('\'');
            sb.append(", img_thumbnail='").append(img_thumbnail).append('\'');
            sb.append(", img_max_resolution='").append(img_max_resolution).append('\'');
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

}
