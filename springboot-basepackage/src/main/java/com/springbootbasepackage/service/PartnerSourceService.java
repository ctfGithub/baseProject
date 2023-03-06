package com.springbootbasepackage.service;

import com.springbootbasepackage.dto.PartnerSourceDTO;

import java.io.InputStream;
import java.util.List;

public interface PartnerSourceService {

    List<PartnerSourceDTO> testPartnerSource();

    void importExcel(InputStream inputStream, Integer channelType);

    Integer save(PartnerSourceDTO partnerSourceDTO);

    String testRedis();
}
