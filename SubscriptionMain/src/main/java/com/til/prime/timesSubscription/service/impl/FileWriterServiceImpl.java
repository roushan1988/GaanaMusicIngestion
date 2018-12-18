package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dao.GaanaDataRepository;
import com.til.prime.timesSubscription.dao.JobRepository;
import com.til.prime.timesSubscription.dao.RawDataRepository;
import com.til.prime.timesSubscription.model.RawMxGaanaDbEntity;
import com.til.prime.timesSubscription.pojo.SearchTuple;
import com.til.prime.timesSubscription.service.EsService;
import com.til.prime.timesSubscription.service.FileWriterService;
import com.til.prime.timesSubscription.service.MailSender;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileWriterServiceImpl implements FileWriterService {

    private static final Logger LOG = Logger.getLogger(FileWriterServiceImpl.class);
    private static final String[] headerList= {"Video S3 Path", "Youtube URL", "Publisher Name", "Video Type", "Album Name", "Album Release Date", "Album Thumbnail Path","Artist Name / Group","Video Title","Audio Language","Singers","Release Date","Genre Level 1","Video Thumbnail","Star", "Gaana ID"};
    private static final String[] headerListNew= {"Video Id", "Youtube Id", "Video Title", "Audio Language", "Gaana Id", "Gaana Track Title", "Album Name", "Release Date","Singers","Star"};

    @Autowired
    private GaanaDataRepository gaanaDao;

    @Autowired
    private RawDataRepository dao;

    @Autowired
    private JobRepository jobDao;

    @Value("${mx.gaana.excel.path.local}")
    private String localFolder;

    @Value("${mx.gaana.email.subject}")
    private String emailSubject;

    @Value("${mx.gaana.email.sendTo}")
    private String recipients;

    @Value("${mx.gaana.email.sendTo.cc}")
    private String ccList;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private EsService esService;

    @Override
//    @Scheduled(cron = "${mx.gaana.sheet.mail.cron}")
    @PostConstruct
    public void prepareExcel(){
//        LOG.info("Starting with file write and mailing");
//        Date date = new Date();
//        JobEntity job = jobDao.findByName(JobEnum.GAANA_SHEET_MAIL_SENDER);
//        boolean success = prepareExcel(TimeUtils.addMillisInDate(job.getEndTime(), -1000), date, job);
//        job.setStartTime(job.getEndTime());
//        job.setEndTime(date);
//        jobDao.save(job);
        List<InputRow> rows = readExcel();
        prepareExcel(rows);
    }

    static class InputRow{
        String videoId;
        String youtubeId;
        String videoTitle;
        String audioLanguage;
        List<RawMxGaanaDbEntity> entities = new ArrayList<>();

        public InputRow(String videoId, String youtubeId, String videoTitle, String audioLanguage) {
            this.videoId = videoId;
            this.youtubeId = youtubeId;
            this.videoTitle = videoTitle;
            this.audioLanguage = audioLanguage;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getYoutubeId() {
            return youtubeId;
        }

        public void setYoutubeId(String youtubeId) {
            this.youtubeId = youtubeId;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getAudioLanguage() {
            return audioLanguage;
        }

        public void setAudioLanguage(String audioLanguage) {
            this.audioLanguage = audioLanguage;
        }

        public List<RawMxGaanaDbEntity> getEntities() {
            return entities;
        }

        public void setEntities(List<RawMxGaanaDbEntity> entities) {
            this.entities = entities;
        }
    }

    public List<InputRow> readExcel(){
        List<InputRow> list = new ArrayList<>();
        try {
            OPCPackage pkg = OPCPackage.open(new File("/Users/roushan.singh1/Downloads/all_music_nogannaID.xlsx"));
//            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/Users/roushan.singh1/Downloads/all_music_nogannaID.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 4; // No of columns

            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    String videoId = row.getCell(0)!=null? row.getCell(0).toString():null;
                    String ytId = row.getCell(2)!=null? row.getCell(2).toString():null;
                    String videoTitle = row.getCell(3)!=null? row.getCell(3).toString():null;
                    String audioLanguage = row.getCell(4)!=null? row.getCell(4).toString():null;
                    list.add(new InputRow(videoId, ytId, videoTitle, audioLanguage));
                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        LOG.info("Size: "+list.size());
        return list;
    }

    private boolean prepareExcel(List<InputRow> rowList){
        try {
            rowList.parallelStream().forEach(i -> {
                SearchTuple tuple = new SearchTuple(i.audioLanguage, i.videoTitle);
                List<RawMxGaanaDbEntity> searchResults = esService.search(tuple);
//                System.out.println("======================================");
//                System.out.println(tuple);
//                if(searchResults==null){
//                    System.out.println(searchResults);
//                }else{
//                    for(RawMxGaanaDbEntity entity: searchResults){
//                        System.out.println(entity);
//                    }
//                }
//                System.out.println("--------------------------------------");
                if(searchResults==null){
                    LOG.error("");
                }
                i.entities.addAll(searchResults);
            });
            LOG.info("ES Search done");
            String gaanaOutputDirectoryPath = new StringBuilder(localFolder).append("/").append("YTtoGaanaSearchResults-").append(System.currentTimeMillis()).append(".xls").toString();
            File gaanaFile = new File(gaanaOutputDirectoryPath);
            gaanaFile.getParentFile().mkdirs();
            try {
                boolean success = writeDetailsToFile(rowList, gaanaFile);
//                if(success){
//                    mailSender.sendMail(emailSubject, "Please find the song upload file attached below.", recipients.split(","), ccList.split(","), new String[]{gaanaOutputDirectoryPath});
//                }
                return success;
            } catch (IOException e) {
                LOG.error("Exception stacktrace: ", e);
                return false;
            }
        }catch (Exception e){
            LOG.error("Exception", e);
            return false;
        }
    }

    private boolean writeDetailsToFile(List<InputRow> rowWithResults, File file) throws IOException {
        if(!rowWithResults.isEmpty()) {
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet worksheet = workbook.createSheet("Sheet1");
                for (int index = 0; index < headerListNew.length; index++) {
                    worksheet.autoSizeColumn(index);
                }
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setWrapText(true);

                HSSFRow header = worksheet.createRow(0);
                addHeaderCells(header, cellStyle);

                int count = 1;
                int rowCount = 1;
                int nullCount = 0;
                for (InputRow resultRow : rowWithResults) {
                    boolean allNull = true;
                    myloop:
                    for(RawMxGaanaDbEntity raw: resultRow.getEntities()){
                        if(raw!=null){
                            allNull = false;
                            break myloop;
                        }
                    }
                    for(int i=0; i<resultRow.entities.size(); i++){
                        HSSFRow row = worksheet.createRow(rowCount);
                        addRowCells(row, cellStyle, resultRow, i);
                        rowCount++;
                    }
                    count++;
                    if(allNull){
                        nullCount++;
                    }
                }
                LOG.info("NullCount: "+nullCount);
                LOG.info("TotalCount: "+(count-1));
                workbook.write(outputStream);
            } catch (Exception e) {
                LOG.error("Exception while writing payment output to file: " + file.getAbsolutePath(), e);
                return false;
            } finally {
                outputStream.flush();
                outputStream.close();
            }
            return true;
        }
        return false;
    }

    private void addHeaderCells(HSSFRow header, CellStyle cellStyle){
        for(int index=0; index<headerListNew.length; index++){
            HSSFCell cell = header.createCell(index);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headerListNew[index]);
        }
    }

    private static final DateFormat dayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void addRowCells(HSSFRow row, CellStyle cellStyle, InputRow input, int resultIndex){
//		"Video S3 Path", "Youtube URL", "Publisher Name", "Video Type", "Album Name", "Album Release Date", "Album Thumbnail Path","Artist Name / Group","Video Title","Audio Language","Singers","Release Date","Genre Level 1","Video Thumbnail","Star", "Gaana ID"
        // "Video Id", "Youtube Id", "Video Title", "Audio Language", "Gaana Id", "Gaana Track Title", "Album Name", "Release Date","Singers","Star"
        if(row!=null) {
            if(resultIndex==0){
                row.createCell(0).setCellValue(input.getVideoId()); // video id
                row.createCell(1).setCellValue(input.getYoutubeId()); // yt id
                row.createCell(2).setCellValue(input.getVideoTitle()); // video title
                row.createCell(3).setCellValue(input.getAudioLanguage()); // audio language
                if(input.getEntities().get(resultIndex)!=null){
                    row.createCell(4).setCellValue(input.getEntities().get(resultIndex).getTrackId()); // gaana id
                    row.createCell(5).setCellValue(input.getEntities().get(resultIndex).getTrackTitle()); // gaana track title
                    row.createCell(6).setCellValue(input.getEntities().get(resultIndex).getAlbumTitle()); // gaana album name
                    if(input.getEntities().get(resultIndex).getReleaseDate()!=null){
                        row.createCell(7).setCellValue(dayDateFormat.format(input.getEntities().get(resultIndex).getReleaseDate())); // release date
                    }else{
                        row.createCell(7).setCellValue(""); // release date
                    }
                    row.createCell(8).setCellValue(input.getEntities().get(resultIndex).getSinger()); // singers
                    StringBuilder sb = new StringBuilder();
                    if (StringUtils.isNotEmpty(input.getEntities().get(resultIndex).getActor())) {
                        sb.append(input.getEntities().get(resultIndex).getActor());
                    }
                    if (StringUtils.isNotEmpty(input.getEntities().get(resultIndex).getActress())) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(input.getEntities().get(resultIndex).getActress());
                    }
                    row.createCell(9).setCellValue(sb.toString()); // star
                }else{
                    row.createCell(4).setCellValue("No Search Result");
                    row.createCell(5).setCellValue("No Search Result");
                    row.createCell(6).setCellValue("No Search Result");
                    row.createCell(7).setCellValue("No Search Result");
                    row.createCell(8).setCellValue("No Search Result");
                    row.createCell(9).setCellValue("No Search Result");
                }
            }else{
                row.createCell(0).setCellValue("----"); // video id
                row.createCell(1).setCellValue("----"); // yt id
                row.createCell(2).setCellValue("----"); // video title
                row.createCell(3).setCellValue("----"); // audio language
                if(input.getEntities().get(resultIndex)!=null){
                    row.createCell(4).setCellValue(input.getEntities().get(resultIndex).getTrackId()); // gaana id
                    row.createCell(5).setCellValue(input.getEntities().get(resultIndex).getTrackTitle()); // gaana track title
                    row.createCell(6).setCellValue(input.getEntities().get(resultIndex).getAlbumTitle()); // gaana album name
                    if(input.getEntities().get(resultIndex).getReleaseDate()!=null){
                        row.createCell(7).setCellValue(dayDateFormat.format(input.getEntities().get(resultIndex).getReleaseDate())); // release date
                    }else{
                        row.createCell(7).setCellValue(""); // release date
                    }
                    row.createCell(8).setCellValue(input.getEntities().get(resultIndex).getSinger()); // singers
                    StringBuilder sb = new StringBuilder();
                    if (StringUtils.isNotEmpty(input.getEntities().get(resultIndex).getActor())) {
                        sb.append(input.getEntities().get(resultIndex).getActor());
                    }
                    if (StringUtils.isNotEmpty(input.getEntities().get(resultIndex).getActress())) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(input.getEntities().get(resultIndex).getActress());
                    }
                    row.createCell(9).setCellValue(sb.toString()); // star
                }else{
                    row.createCell(4).setCellValue("No Search Result");
                    row.createCell(5).setCellValue("No Search Result");
                    row.createCell(6).setCellValue("No Search Result");
                    row.createCell(7).setCellValue("No Search Result");
                    row.createCell(8).setCellValue("No Search Result");
                    row.createCell(9).setCellValue("No Search Result");
                }
            }
            for (int i = 0; i < headerListNew.length; i++) {
                row.getCell(i).setCellStyle(cellStyle);
            }
        }
    }
}
