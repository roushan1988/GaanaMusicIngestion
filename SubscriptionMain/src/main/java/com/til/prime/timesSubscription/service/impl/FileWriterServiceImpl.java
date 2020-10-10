package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dao.GaanaDataRepository;
import com.til.prime.timesSubscription.dao.JobRepository;
import com.til.prime.timesSubscription.dao.RawDataRepository;
import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import com.til.prime.timesSubscription.service.EsService;
import com.til.prime.timesSubscription.service.FileWriterService;
import com.til.prime.timesSubscription.service.MXVideoTestService;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileWriterServiceImpl implements FileWriterService {

    private static final Logger LOG = Logger.getLogger(FileWriterServiceImpl.class);
    private static final String[] headerList= {"Video S3 Path", "Youtube URL", "Publisher Name", "Video Type", "Album Name", "Album Release Date", "Album Thumbnail Path","Artist Name / Group","Video Title","Audio Language","Singers","Release Date","Genre Level 1","Video Thumbnail","Star", "Gaana ID"};
//    private static final String[] headerListNew= {"Video Id", "Youtube Id", "Video Title", "Audio Language", "Gaana Id", "Gaana Track Title", "Album Name", "Release Date","Singers","Star"};

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
    private MXVideoTestService testService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private EsService esService;

    @Override
//    @Scheduled(cron = "${mx.gaana.sheet.mail.cron}")
//    @PostConstruct
    public void readExcel() throws Exception{
//        LOG.info("Starting with file write and mailing");
//        Date date = new Date();
//        JobEntity job = jobDao.findByName(JobEnum.GAANA_SHEET_MAIL_SENDER);
//        boolean success = prepareExcel(TimeUtils.addMillisInDate(job.getEndTime(), -1000), date, job);
//        job.setStartTime(job.getEndTime());
//        job.setEndTime(date);
//        jobDao.save(job);
        int sheet = 0;
        String language = "English";
        List<InputRow> rows = readExcel(sheet);
        List<MxGaanaDbEntity> entities = new ArrayList<>();
        int errorCount = 0;
        for(InputRow row: rows){
            MxGaanaDbEntity entity = new MxGaanaDbEntity();
            entity.setTrackId(row.gaanaId);
            entity.setYoutubeId(row.youtubeId);
            entity.setCreated(new Date());
            entity.setLanguage(language);
            entities.add(entity);
            try {
                gaanaDao.save(entity);
            }catch (Exception e){
                LOG.error("Error while saving", e);
                errorCount++;
            }
        }
        LOG.info("Error count: "+errorCount);
//        entities = gaanaDao.saveAll(entities);
        LOG.info("Done");
    }

    static class InputRow{
        long gaanaId;
        String youtubeId;
//        String videoTitle;
//        String audioLanguage;
//        List<RawMxGaanaDbEntity> entities = new ArrayList<>();

        public InputRow(long gaanaId, String youtubeId) {
            this.gaanaId = gaanaId;
            this.youtubeId = youtubeId;
//            this.videoTitle = videoTitle;
//            this.audioLanguage = audioLanguage;
        }

        public long getGaanaId() {
            return gaanaId;
        }

        public void setGaanaId(long gaanaId) {
            this.gaanaId = gaanaId;
        }

        public String getYoutubeId() {
            return youtubeId;
        }

        public void setYoutubeId(String youtubeId) {
            this.youtubeId = youtubeId;
        }
    }

    public List<InputRow> readExcel(int sheetNumber){
        List<InputRow> list = new ArrayList<>();
        try {
            OPCPackage pkg = OPCPackage.open(new File("/Users/roushan.singh1/Desktop/CompilationofWeek5.xlsx"));
//            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/Users/roushan.singh1/Downloads/all_music_nogannaID.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet = wb.getSheetAt(sheetNumber);
            XSSFRow row;
            XSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

//            int cols = 4; // No of columns
            int count=0;
            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
//                    row.getCell(15).setCellType(Cell.CELL_TYPE_NUMERIC);
                    if(row.getCell(1)!=null && StringUtils.isNotEmpty(row.getCell(1).toString()) && row.getCell(15)!=null){
                        count++;
                    }
                    try {
                        if (row.getCell(1) != null && StringUtils.isNotEmpty(row.getCell(1).toString()) && row.getCell(15) != null) {
                            String youtubeUrl = row.getCell(1).toString();
                            long gaanaId = new BigDecimal(row.getCell(15).getNumericCellValue()).longValue();
                            list.add(new InputRow(gaanaId, youtubeUrl));
                        }
                    }catch (Exception e){
                        LOG.info("row: "+r);
//                        LOG.error("", e);
                    }
//                    String videoId = row.getCell(0)!=null? row.getCell(0).toString():null;
//                    String ytId = row.getCell(2)!=null? row.getCell(2).toString():null;
//                    String videoTitle = row.getCell(3)!=null? row.getCell(3).toString():null;
//                    String audioLanguage = row.getCell(4)!=null? row.getCell(4).toString():null;
//                    list.add(new InputRow(videoId, ytId, videoTitle, audioLanguage));
                }
            }
            System.out.println(count);
        } catch(Exception ioe) {
            LOG.error("", ioe);
        }
        LOG.info("Size: "+list.size());
        return list;
    }

    public boolean prepareExcel(List<MxGaanaDbEntity> rowList){
        try {
            String gaanaOutputDirectoryPath = new StringBuilder(localFolder).append("/").append("ManualUploadSheet-").append(dayDateFormat.format(new Date())).append("-").append(rowList.size()).append(".xls").toString();
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

    private boolean writeDetailsToFile(List<MxGaanaDbEntity> gaanaDbEntities, File file) throws IOException {
        if(!gaanaDbEntities.isEmpty()) {
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet worksheet = workbook.createSheet("Sheet1");
                for (int index = 0; index < headerList.length; index++) {
                    worksheet.autoSizeColumn(index);
                }
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setWrapText(true);

                HSSFRow header = worksheet.createRow(0);
                addHeaderCells(header, cellStyle);

                int count = 1;
                for (MxGaanaDbEntity entity : gaanaDbEntities) {
                    HSSFRow row = worksheet.createRow(count);
                    addRowCells(row, cellStyle, entity);
                    count++;
                }
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
        for(int index=0; index<headerList.length; index++){
            HSSFCell cell = header.createCell(index);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headerList[index]);
        }
    }

    private static final DateFormat dayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void addRowCells(HSSFRow row, CellStyle cellStyle, MxGaanaDbEntity entity){
//		"Video S3 Path", "Youtube URL", "Publisher Name", "Video Type", "Album Name", "Album Release Date", "Album Thumbnail Path","Artist Name / Group","Video Title","Audio Language","Singers","Release Date","Genre Level 1","Video Thumbnail","Star", "Gaana ID"
        if(row!=null) {
            row.createCell(0).setCellValue(""); // video s3 path
            row.createCell(1).setCellValue(entity.getYoutubeId()); // yt url
            row.createCell(2).setCellValue(entity.getLabel()); // publisher name
            row.createCell(3).setCellValue("Music"); // video type
            row.createCell(4).setCellValue(entity.getAlbumTitle()); // album name
            if (entity.getAlbumReleaseDate() != null) {
                row.createCell(5).setCellValue(dayDateFormat.format(entity.getAlbumReleaseDate())); // album release date
            }else if(entity.getReleaseDate() != null){
                row.createCell(5).setCellValue(dayDateFormat.format(entity.getReleaseDate()));
            }else{
                row.createCell(5).setCellValue("");
            }
            row.createCell(6).setCellValue(entity.getS3AlbumThumbnailPath());//find album thumbnail path
            row.createCell(7).setCellValue(new StringBuilder("Various Artists").append(" - ").append(entity.getLanguage()).toString()); // artist name group
            row.createCell(8).setCellValue(entity.getTrackTitle()); // video title
            row.createCell(9).setCellValue(entity.getLanguage()); // audit language
            row.createCell(10).setCellValue(entity.getSinger()); // singers
            if (entity.getReleaseDate() != null) {
                row.createCell(11).setCellValue(dayDateFormat.format(entity.getReleaseDate())); // release date
            }else{
                row.createCell(11).setCellValue("");
            }
            row.createCell(12).setCellValue(entity.getGenres()); // genre
            row.createCell(13).setCellValue(entity.getS3VideoThumbnailPath()); // video thumbnail
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotEmpty(entity.getActor())) {
                sb.append(entity.getActor());
            }
            if (StringUtils.isNotEmpty(entity.getActress())) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(entity.getActress());
            }
            row.createCell(14).setCellValue(sb.toString()); // star
            row.createCell(15).setCellValue(entity.getTrackId()); // gaana id
            for (int i = 0; i < headerList.length; i++) {
                if(row==null || row.getCell(i)==null || cellStyle==null){
                    System.out.println("here");
                }
                row.getCell(i).setCellStyle(cellStyle);
            }
        }
    }
}
