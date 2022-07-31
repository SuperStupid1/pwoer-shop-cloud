package com.powernode.service;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/30 13:10
 */
public interface EsImportService {

    /**
     * 全量导入
     */
    void importAll();


    /**
     * 增量导入
     */
    void updateImport();


    /**
     * 快速导入
     */
    void quickImport();
}
