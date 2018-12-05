package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dao.GaanaDataRepository;
import com.til.prime.timesSubscription.dao.JobRepository;
import com.til.prime.timesSubscription.enums.JobEnum;
import com.til.prime.timesSubscription.model.JobEntity;
import com.til.prime.timesSubscription.model.MxGaanaDbEntity;
import com.til.prime.timesSubscription.service.FileWriterService;
import com.til.prime.timesSubscription.service.MailSender;
import com.til.prime.timesSubscription.util.TimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileWriterServiceImpl implements FileWriterService {

    private static final Logger LOG = Logger.getLogger(FileWriterServiceImpl.class);
    private static final String[] headerList= {"Video S3 Path", "Youtube URL", "Publisher Name", "Video Type", "Album Name", "Album Release Date", "Album Thumbnail Path","Artist Name / Group","Video Title","Audio Language","Singers","Release Date","Genre Level 1","Video Thumbnail","Star", "Gaana ID"};

    @Autowired
    private GaanaDataRepository gaanaDao;

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

    @Override
    @Scheduled(cron = "${mx.gaana.sheet.mail.cron}")
    public void prepareExcel(){
        LOG.info("Starting with file write and mailing");
        Date date = new Date();
        JobEntity job = jobDao.findByName(JobEnum.GAANA_SHEET_MAIL_SENDER);
        boolean success = prepareExcel(TimeUtils.addMillisInDate(job.getEndTime(), -1000), date, job);
        job.setStartTime(job.getEndTime());
        job.setEndTime(date);
        jobDao.save(job);
    }

    private boolean prepareExcel(Date start, Date end, JobEntity job){
        try {
            int page = 0;
            List<MxGaanaDbEntity> list = new ArrayList<>();
            List<MxGaanaDbEntity> list2 = new ArrayList<>();
            loop1:
            while (true) {
                try {
                    List<MxGaanaDbEntity> models = gaanaDao.findByYoutubeIdNotNullAndUpdatedBetweenOrderByPopularityIndexDesc(start, end, new PageRequest(page, 1000));
                    page++;
                    if (CollectionUtils.isEmpty(models)) {
                        break loop1;
                    }
                    list.addAll(models);
                } catch (Exception e) {
                    LOG.error("Exception", e);
                }
            }
            for (MxGaanaDbEntity entity : list) {
                if (!entity.getYoutubeId().equals("NA") && StringUtils.isNotEmpty(entity.getS3AlbumThumbnailPath()) && StringUtils.isNotEmpty(entity.getS3VideoThumbnailPath())) {
                    list2.add(entity);
                }
            }
            job.setRecordCount(list2.size());
            String gaanaOutputDirectoryPath = new StringBuilder(localFolder).append("/")
                    .append(list2.get(0).getPopularityIndex()).append("-").append(list2.get(list2.size()-1).getPopularityIndex()).append("-").append(System.currentTimeMillis()).append(".xls").toString();
            File gaanaFile = new File(gaanaOutputDirectoryPath);
            gaanaFile.getParentFile().mkdirs();
            try {
                boolean success = writeDetailsToFile(list2, gaanaFile);
                if(success){
                    mailSender.sendMail(emailSubject, "Please find the song upload file attached below.", recipients.split(","), ccList.split(","), new String[]{gaanaOutputDirectoryPath});
                }
                return success;
            } catch (IOException e) {
                LOG.error("Exception stacktrace: ", e);
                return false;
            }
        }catch (Exception e){
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
