package com.barco.common.manager.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DatabaseExchange {

    private Logger logger = LogManager.getLogger(DatabaseExchange.class);

}
