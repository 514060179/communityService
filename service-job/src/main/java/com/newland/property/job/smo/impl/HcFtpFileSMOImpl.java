package com.newland.property.job.smo.impl;

import com.newland.property.job.model.FtpTaskLog;
import com.newland.property.job.model.FtpTaskLogDetail;
import com.newland.property.job.smo.IHcFtpFileBMO;
import com.newland.property.job.smo.IHcFtpFileSMO;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
@Service("hcFtpFileSMOImpl")
@Transactional
public class HcFtpFileSMOImpl implements IHcFtpFileSMO {
    /** logger */
	private static final Logger logger = LoggerFactory.getLogger(HcFtpFileSMOImpl.class);


	@Resource(name = "hcFtpFileBMOImpl")
	private IHcFtpFileBMO hcFtpFileBMOImpl;

	@Override
	public long saveTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		return hcFtpFileBMOImpl.saveTaskRunLog(loginfo);
	}

	@Override
    public void updateTaskRunLog(FtpTaskLog loginfo) {
		// TODO Auto-generated method stub
		hcFtpFileBMOImpl.updateTaskRunLog(loginfo);
	}
	@Override
	public int saveTaskRunDetailLog(FtpTaskLogDetail logdetail) {
		// TODO Auto-generated method stub
		return hcFtpFileBMOImpl.saveTaskRunDetailLog(logdetail);
	}

	/**
	 * 执行存过，处理任务执行前后的事情
	 */
	@Override
    public void saveDbFunction(String function){
		hcFtpFileBMOImpl.saveDbFunction(function);
	}
	/**
	 * 执行存过(带参数)，处理任务执行前后的事情
	 */
	@Override
    public void saveDbFunctionWithParam(Map info){
		hcFtpFileBMOImpl.saveDbFunctionWithParam(info);
	}

	@Override
	public void insertFileData2Table(String insertSQL) {
		// TODO Auto-generated method stub
		hcFtpFileBMOImpl.insertFileData2Table(insertSQL);
	}
}