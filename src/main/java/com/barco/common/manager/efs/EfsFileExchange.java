package com.barco.common.manager.efs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EfsFileExchange {

    private Logger logger = LogManager.getLogger(EfsFileExchange.class);

}
