package com.newland.property.common.smo.impl;

import com.newland.property.common.dao.IFileServiceDao;
import com.newland.property.config.properties.code.NewlandPropertyProperties;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.core.client.CosUploadTemplate;
import com.newland.property.core.client.FtpUploadTemplate;
import com.newland.property.core.client.JSchFtpUploadTemplate;
import com.newland.property.core.client.OssUploadTemplate;
import com.newland.property.dto.file.FileDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.util.COSUtil;
import com.newland.property.utils.util.OSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FileInnerServiceSMOImpl extends BaseServiceSMO implements IFileInnerServiceSMO {

    private static final String ROOT_PATH = "hc/";

    @Autowired
    private IFileServiceDao fileServiceDaoImpl;

    @Autowired
    private NewlandPropertyProperties newlandPropertyProperties;

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private JSchFtpUploadTemplate jSchFtpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;

    @Override
    public String saveFile(@RequestBody FileDto fileDto) {

        //int saveFileFlag = fileServiceDaoImpl.saveFile(BeanConvertUtil.beanCovertMap(fileDto));
        String fileName = "";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, OSSUtil.OSS_SWITCH);

        if (OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            fileName = ossUploadTemplate.upload(fileDto.getContext(), newlandPropertyProperties.getFtpServer(),
                    newlandPropertyProperties.getFtpPort(), newlandPropertyProperties.getFtpUserName(),
                    newlandPropertyProperties.getFtpUserPassword(), ROOT_PATH);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            fileName = cosUploadTemplate.upload(fileDto.getContext(), newlandPropertyProperties.getFtpServer(),
                    newlandPropertyProperties.getFtpPort(), newlandPropertyProperties.getFtpUserName(),
                    newlandPropertyProperties.getFtpUserPassword(), ROOT_PATH);
        } else {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);
            //String ftpPath = "hc/";

            fileName = ftpUploadTemplate.upload(fileDto.getContext(), ftpServer,
                        ftpPort, ftpUserName,
                        ftpUserPassword, ROOT_PATH);
        }
        return fileName;
    }

    @Override
    public List<FileDto> queryFiles(@RequestBody FileDto fileDto) {
        //return BeanConvertUtil.covertBeanList(fileServiceDaoImpl.getFiles(BeanConvertUtil.beanCovertMap(fileDto)), FileDto.class);
        List<FileDto> fileDtos = new ArrayList<>();
        String fileName = fileDto.getFileSaveName();
        String ftpPath = "hc/";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileName.contains("/")) {
            ftpPath += fileName.substring(0, fileName.lastIndexOf("/") + 1);
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
        }
        String context = "";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, OSSUtil.OSS_SWITCH);
        if (OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            context = ossUploadTemplate.download(ftpPath, fileName, newlandPropertyProperties.getFtpServer(),
                    newlandPropertyProperties.getFtpPort(), newlandPropertyProperties.getFtpUserName(),
                    newlandPropertyProperties.getFtpUserPassword());
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            context = cosUploadTemplate.download(ftpPath, fileName, newlandPropertyProperties.getFtpServer(),
                    newlandPropertyProperties.getFtpPort(), newlandPropertyProperties.getFtpUserName(),
                    newlandPropertyProperties.getFtpUserPassword());
        } else {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);
            context = ftpUploadTemplate.download(ftpPath, fileName, ftpServer,
                    ftpPort, ftpUserName,
                    ftpUserPassword);
        }

        fileDto.setContext(context);
        fileDto.setSuffix(suffix);
        fileDtos.add(fileDto);
        return fileDtos;
    }

    public IFileServiceDao getFileServiceDaoImpl() {
        return fileServiceDaoImpl;
    }

    public void setFileServiceDaoImpl(IFileServiceDao fileServiceDaoImpl) {
        this.fileServiceDaoImpl = fileServiceDaoImpl;
    }
}
