package com.lsy.storage;

/**
 * Description:
 */
public interface IDBKey {

    /**
     * 存在数据库是String（可以是JSONString 或者就是 String）数据的表名
     */
    String DB_NAME_PUB = "string_db_name";

    /**
     * 存数据的表名 之后建立的数据尽量用这个表
     */
    String DB_NAME_DATA = "name_data";

}
