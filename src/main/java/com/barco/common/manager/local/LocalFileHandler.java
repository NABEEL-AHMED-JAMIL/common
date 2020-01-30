package com.barco.common.manager.local;

import com.barco.common.util.ExceptionUtil;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.*;

/**
 * @author Nabeel.amd
 */
@Component
@Scope("prototype")
public class LocalFileHandler {

    public Logger logger = LogManager.getLogger(LocalFileHandler.class);

    @Value("${file.downloadLocation}")
    private String basePathTempDire;

    public LocalFileHandler() {}

    public boolean makeDir(String basePath) {
        try {
            File finalDir = new File(basePath);
            if (!finalDir.exists()) {
                logger.info("Making New Directory at path [ " + basePath + " ]");
                return finalDir.mkdirs();
            } else {
                logger.info("Directory Already Exist At Path [ " + basePath + " ]");
                return true;
            }
        } catch (Exception ex) {
            logger.error("Exception :- " + ExceptionUtil.getRootCauseMessage(ex));
        }
        return false;
    }

    // raw-content mean the text,csv
    public boolean saveRawContentIntoFile(String filePath, Object object) {
        try {
            logger.info("Raw Data Save Into File {}", filePath);
            final OutputStream os = new FileOutputStream(filePath);
            final PrintStream printStream = new PrintStream(os);
            printStream.print(object);
            printStream.close();
            logger.info("Raw Data Save Successfully Into File {}", filePath);
            return true;
        } catch (Exception ex) {
            logger.error("Exception :- " + ExceptionUtil.getRootCauseMessage(ex));
        }
        return false;
    }

    public void deleteDir(String basePath) {
        try {
            File file = new File(basePath);
            if (file.exists()) {
                logger.info("Deleting Directory At Path [ " + basePath + " ]");
                FileUtils.deleteDirectory(file);
            }
        } catch (Exception ex) {
            logger.error("Exception :- " + ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    public String getBasePathTempDire() { return basePathTempDire; }
    public void setBasePathTempDire(String basePathTempDire) {
        this.basePathTempDire = basePathTempDire;
    }

    @Override
    public String toString() { return new Gson().toJson(this); }

}
